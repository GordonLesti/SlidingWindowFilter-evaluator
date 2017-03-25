package swf.calculator.distance;

import swf.calculator.Distance;
import swf.calculator.Measure;

public class MaxMinQuotient<T> implements Distance<Double, T> {
  private Measure<Double, T> measure;

  public MaxMinQuotient(Measure<Double, T> measure) {
    this.measure = measure;
  }

  /**
   * Returns the quotient between the max and min value of the measure from both objects.
   */
  public Double calculateDistance(T obj1, T obj2) {
    Double value1 = this.measure.calculate(obj1);
    Double value2 = this.measure.calculate(obj2);
    return Math.max(value1, value2) / Math.min(value1, value2);
  }
}
