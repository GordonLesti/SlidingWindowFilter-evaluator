package swf.transformer;

import java.lang.IllegalArgumentException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import swf.calculator.Mean;
import swf.model.TimeSeries;
import swf.model.timeseries.Item;

public class MeanTransformer<T> implements TimeSeriesTransformer<T> {
  private int windowSize;
  private int stepSize;
  private Mean<T> meanCalculator;

  /**
   * Creates a new MeanTransformer for TimeSeries objects.
   */
  public MeanTransformer(int windowSize, int stepSize, Mean<T> meanCalculator) {
    this.windowSize = windowSize;
    this.stepSize = stepSize;
    this.meanCalculator = meanCalculator;
  }

  /**
   * Creates a reduced TimeSeries with calculated mean items of the given TimeSeries.
   */
  public TimeSeries<T> transform(TimeSeries<T> timeSeries) {
    List<Item<T>> itemList = timeSeries.getItems();
    List<Item<T>> meanList = new LinkedList<Item<T>>();
    ListIterator<Item<T>> listIterator = itemList.listIterator();
    LinkedList<T> tempDataList = new LinkedList<T>();
    long fromTime = listIterator.next().getTime();
    listIterator.previous();
    long toTime = fromTime + this.windowSize;
    while (listIterator.hasNext()) {
      Item<T> item = listIterator.next();
      long time = item.getTime();
      if (time < fromTime) {
        throw new IllegalArgumentException("TimeSeries is not in chronicle order.");
      }
      if (time < toTime) {
        tempDataList.add(item.getData());
      } else {
        if (tempDataList.size() == 0) {
          throw new IllegalArgumentException("TimeSeries is incomplete.");
        }
        meanList.add(new Item<T>(fromTime, this.meanCalculator.calculateMean(tempDataList)));
        tempDataList = new LinkedList<T>();
        fromTime += this.stepSize;
        toTime = fromTime + this.windowSize;
        boolean backInTime = true;
        while (backInTime && listIterator.hasPrevious()) {
          if (listIterator.previous().getTime() < fromTime) {
            backInTime = false;
          }
        }
        if (listIterator.hasNext()) {
          listIterator.next();
        }
      }
    }
    if (!tempDataList.isEmpty()) {
      meanList.add(new Item<T>(fromTime, this.meanCalculator.calculateMean(tempDataList)));
    }
    return new TimeSeries<T>(meanList, timeSeries.getFlags());
  }
}
