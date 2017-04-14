package swf.measure.timeseries;

import swf.TimeSeries;
import swf.measure.Distance;
import swf.measure.Estimate;

public class MaxMinQuotient<T> implements Distance<TimeSeries<T>> {
  private Estimate<TimeSeries<T>> estimate;

  public MaxMinQuotient(Estimate<TimeSeries<T>> estimate) {
    this.estimate = estimate;
  }

  /**
   * Returns the quotient of the max and min estimate.
   */
  public double distance(TimeSeries<T> ts1, TimeSeries<T> ts2) {
    double est1 = this.estimate.estimate(ts1);
    double est2 = this.estimate.estimate(ts2);
    if (est1 > est2) {
      return est1 / est2;
    }
    return est2 / est1;
  }
}
