package swf.calculator.filter;

import java.util.Iterator;
import java.util.List;
import swf.calculator.Filter;
import swf.calculator.Measure;
import swf.model.TimeSeries;

public class MinMaxTimeSeriesFilter<T> implements Filter<TimeSeries<T>> {
  private Double min;
  private Double max;
  private Measure<Double, TimeSeries<T>> measure;

  /**
   * Creates a filter that checks if a TimeSeries measure value is in the interval of
   * the min and max measure multiplied with a blurFactor of a list of TimeSeries.
   */
  public MinMaxTimeSeriesFilter(
      Measure<Double, TimeSeries<T>> measure,
      List<TimeSeries<T>> timeSeriesList,
      double blurFactor
  ) {
    this.measure = measure;
    double min = Double.MAX_VALUE;
    double max = Double.MIN_VALUE;
    Iterator<TimeSeries<T>> iterator = timeSeriesList.iterator();
    while (iterator.hasNext()) {
      Double measureValue = this.measure.calculate(iterator.next());
      if (measureValue < min) {
        min = measureValue;
      }
      if (measureValue > max) {
        max = measureValue;
      }
    }
    double length = Math.abs(max - min) * blurFactor;
    this.max = min + length;
    this.min = max - length;
  }

  /**
   * Checks if the TimeSeries measure is in the interval.
   */
  public boolean filter(TimeSeries<T> timeSeries) {
    Double measureValue = this.measure.calculate(timeSeries);
    if (measureValue >= this.min && measureValue <= this.max) {
      return true;
    }
    return false;
  }
}
