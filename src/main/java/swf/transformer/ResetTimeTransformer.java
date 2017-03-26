package swf.transformer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import swf.model.TimeSeries;
import swf.model.timeseries.Item;

public class ResetTimeTransformer<T> implements TimeSeriesTransformer<T> {

  /**
   * Resets the time in all items and flags to the minimum time.
   */
  public TimeSeries<T> transform(TimeSeries<T> timeSeries) {
    List<Item<T>> items = timeSeries.getItems();
    List<Item<String>> flags = timeSeries.getFlags();
    long min = Long.MAX_VALUE;
    Iterator<Item<T>> itemIterator = items.iterator();
    while (itemIterator.hasNext()) {
      long time = itemIterator.next().getTime();
      if (time < min) {
        min = time;
      }
    }
    Iterator<Item<String>> flagIterator = flags.iterator();
    while (flagIterator.hasNext()) {
      long time = flagIterator.next().getTime();
      if (time < min) {
        min = time;
      }
    }
    LinkedList<Item<T>> newItemList = new LinkedList<Item<T>>();
    itemIterator = items.iterator();
    while (itemIterator.hasNext()) {
      Item<T> item = itemIterator.next();
      newItemList.add(new Item<T>(item.getTime() - min, item.getData()));
    }
    LinkedList<Item<String>> newFlagList = new LinkedList<Item<String>>();
    flagIterator = flags.iterator();
    while (flagIterator.hasNext()) {
      Item<String> item = flagIterator.next();
      newFlagList.add(new Item<String>(item.getTime() - min, item.getData()));
    }
    return new TimeSeries<T>(newItemList, newFlagList);
  }
}
