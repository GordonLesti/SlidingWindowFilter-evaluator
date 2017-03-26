package swf.transformer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import swf.model.TimeSeries;
import swf.model.timeseries.Item;

public class SubWindowTransformer<T> implements TimeSeriesTransformer<T> {
  private long fromTime;
  private long toTime;

  public SubWindowTransformer(long fromTime, long toTime) {
    this.fromTime = fromTime;
    this.toTime = toTime;
  }

  /**
   * Creates a new TimeSeries with all items between including fromTime and excluding toTime.
   */
  public TimeSeries<T> transform(TimeSeries<T> timeSeries) {
    Iterator<Item<T>> itemIterator = timeSeries.getItems().iterator();
    LinkedList<Item<T>> newItemList = new LinkedList<Item<T>>();
    while (itemIterator.hasNext()) {
      Item<T> item = itemIterator.next();
      long time = item.getTime();
      if (time >= this.fromTime && time < this.toTime) {
        newItemList.add(item);
      }
    }
    Iterator<Item<String>> flagIterator = timeSeries.getFlags().iterator();
    LinkedList<Item<String>> newFlagList = new LinkedList<Item<String>>();
    while (flagIterator.hasNext()) {
      Item<String> item = flagIterator.next();
      long time = item.getTime();
      if (time >= this.fromTime && time < this.toTime) {
        newFlagList.add(item);
      }
    }
    return new TimeSeries<T>(newItemList, newFlagList);
  }
}
