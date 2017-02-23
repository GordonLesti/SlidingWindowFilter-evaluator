package swf.transformer.accel;

import java.lang.IllegalArgumentException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import swf.model.AccelerationData;
import swf.model.TimeSeries;
import swf.model.timeseries.Item;
import swf.transformer.TimeSeriesTransformer;

public class MeanTransformer implements TimeSeriesTransformer<AccelerationData> {
  private int windowSize;
  private int stepSize;

  public MeanTransformer(int windowSize, int stepSize) {
    this.windowSize = windowSize;
    this.stepSize = stepSize;
  }

  /**
   * Filters the given TimeSeries and means the AccelerationData.
   */
  public TimeSeries<AccelerationData> transform(TimeSeries<AccelerationData> timeSeries) {
    List<Item<AccelerationData>> accelList = timeSeries.getItems();
    List<Item<AccelerationData>> meanList = new LinkedList<Item<AccelerationData>>();
    ListIterator<Item<AccelerationData>> listIterator = accelList.listIterator();
    int sumX = 0;
    int sumY = 0;
    int sumZ = 0;
    int itemCount = 0;
    long fromTime = listIterator.next().getTime();
    listIterator.previous();
    long toTime = fromTime + this.windowSize;
    while (listIterator.hasNext()) {
      Item<AccelerationData> item = listIterator.next();
      AccelerationData accel = item.getData();
      long time = item.getTime();
      if (time < fromTime) {
        throw new IllegalArgumentException("TimeSeries is not in chronicle order.");
      }
      if (time < toTime) {
        sumX += accel.getX();
        sumY += accel.getY();
        sumZ += accel.getZ();
        itemCount++;
      } else {
        if (itemCount < 1) {
          throw new IllegalArgumentException("TimeSeries is incomplete.");
        }
        meanList.add(
            new Item<AccelerationData>(
                fromTime,
                new AccelerationData(
                    this.mean(sumX, itemCount),
                    this.mean(sumY, itemCount),
                    this.mean(sumZ, itemCount)
                )
            )
        );
        sumX = 0;
        sumY = 0;
        sumZ = 0;
        itemCount = 0;
        fromTime += stepSize;
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
    if (itemCount > 0) {
      meanList.add(
          new Item<AccelerationData>(
              fromTime,
              new AccelerationData(
                  this.mean(sumX, itemCount),
                  this.mean(sumY, itemCount),
                  this.mean(sumZ, itemCount)
              )
          )
      );
    }
    return new TimeSeries<AccelerationData>(meanList, timeSeries.getFlags());
  }

  private static int mean(int sum, int count) {
    return (int)Math.round((sum * 1.0) / count);
  }
}
