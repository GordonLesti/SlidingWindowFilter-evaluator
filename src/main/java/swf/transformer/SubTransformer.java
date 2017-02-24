package swf.transformer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import swf.model.TimeSeries;
import swf.model.timeseries.Item;

public class SubTransformer<T> implements TimeSeriesTransformer<T> {
  private String fromFlag;
  private String toFlag;

  public SubTransformer(String fromFlag, String toFlag) {
    this.fromFlag = fromFlag;
    this.toFlag = toFlag;
  }

  /**
   * Filters all items and flags that are not between a from and to flag.
   */
  public TimeSeries<T> transform(TimeSeries<T> timeSeries) {
    long[] fromAndToTime = this.getFromAndToTime(timeSeries.getFlags());
    Iterator<Item<T>> itemIterator = timeSeries.getItems().iterator();
    LinkedList<Item<T>> subItemList = new LinkedList<Item<T>>();
    while (itemIterator.hasNext()) {
      Item<T> item = itemIterator.next();
      long time = item.getTime();
      if (time >= fromAndToTime[0] && time <= fromAndToTime[1]) {
        subItemList.add(item);
      }
    }
    Iterator<Item<String>> flagIterator = timeSeries.getFlags().iterator();
    LinkedList<Item<String>> subFlagList = new LinkedList<Item<String>>();
    while (flagIterator.hasNext()) {
      Item<String> item = flagIterator.next();
      long time = item.getTime();
      if (time >= fromAndToTime[0] && time <= fromAndToTime[1]) {
        subFlagList.add(item);
      }
    }
    return new TimeSeries<T>(subItemList, subFlagList);
  }

  private long[] getFromAndToTime(List<Item<String>> flags) {
    long[] fromAndToTime = new long[2];
    fromAndToTime[0] = -1;
    fromAndToTime[1] = -1;
    Iterator<Item<String>> iterator = flags.iterator();
    while (iterator.hasNext()) {
      Item<String> item = iterator.next();
      String data = item.getData();
      if (data.equals(this.fromFlag)) {
        if (fromAndToTime[0] != -1) {
          throw new IllegalArgumentException("TimeSeries has duplicate flags.");
        }
        fromAndToTime[0] = item.getTime();
      }
      if (data.equals(this.toFlag)) {
        if (fromAndToTime[1] != -1) {
          throw new IllegalArgumentException("TimeSeries has duplicate flags.");
        }
        fromAndToTime[1] = item.getTime();
      }
    }
    if (fromAndToTime[0] == -1 || fromAndToTime[1] == -1) {
      throw new IllegalArgumentException("Flag not found in TimeSeries.");
    }
    if (fromAndToTime[0] > fromAndToTime[1]) {
      throw new IllegalArgumentException("From flag is after to flag.");
    }
    return fromAndToTime;
  }
}
