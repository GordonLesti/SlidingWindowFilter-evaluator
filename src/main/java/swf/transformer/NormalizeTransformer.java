package swf.transformer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import swf.calculator.Subtraction;
import swf.calculator.measure.Mean;
import swf.model.TimeSeries;
import swf.model.timeseries.Item;

public class NormalizeTransformer<T> implements TimeSeriesTransformer<T> {
  private Subtraction<T> subtraction;
  private Mean<T> meanCalculator;

  public NormalizeTransformer(Subtraction<T> subtraction, Mean<T> meanCalculator) {
    this.subtraction = subtraction;
    this.meanCalculator = meanCalculator;
  }

  /**
   * Subtract the mean of the items from every item in the TimeSeries.
   */
  public TimeSeries<T> transform(TimeSeries<T> timeSeries) {
    LinkedList<T> itemDataList = new LinkedList<T>();
    Iterator<Item<T>> itemIterator = timeSeries.getItems().iterator();
    while (itemIterator.hasNext()) {
      itemDataList.add(itemIterator.next().getData());
    }
    T mean = this.meanCalculator.calculate(itemDataList);
    itemIterator = timeSeries.getItems().iterator();
    LinkedList<Item<T>> newItemList = new LinkedList<Item<T>>();
    while (itemIterator.hasNext()) {
      Item<T> item = itemIterator.next();
      newItemList.add(new Item<T>(item.getTime(), this.subtraction.subtract(item.getData(), mean)));
    }
    return new TimeSeries<T>(newItemList, timeSeries.getFlags());
  }
}
