package swf.calculator.measure;

import java.lang.IllegalArgumentException;
import java.util.Iterator;
import java.util.List;
import swf.calculator.Distance;
import swf.calculator.Measure;
import swf.model.TimeSeries;
import swf.model.timeseries.Item;

public class Complexity<T> implements Measure<Double, TimeSeries<T>> {
  private Distance<Double, T> distanceCalculator;

  public Complexity(Distance<Double, T> distanceCalculator) {
    this.distanceCalculator = distanceCalculator;
  }

  /**
   * Calculates the complexity of a timeSeries and returns it as Double.
   */
  public Double calculate(TimeSeries<T> timeSeries) {
    double complexity = 0;
    List<Item<T>> items = timeSeries.getItems();
    Iterator<Item<T>> iterator = items.iterator();
    if (!iterator.hasNext()) {
      throw new IllegalArgumentException("TimeSeries is empty");
    }
    Item<T> prevoisItem = iterator.next();
    while (iterator.hasNext()) {
      Item<T> item = iterator.next();
      double distance = this.distanceCalculator.calculateDistance(
          prevoisItem.getData(),
          item.getData()
      );
      complexity += distance * distance;
      prevoisItem = item;
    }
    return Math.sqrt(complexity);
  }
}
