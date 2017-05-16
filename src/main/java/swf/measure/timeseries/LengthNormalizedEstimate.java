package swf.measure.timeseries;

import swf.TimeSeries;
import swf.measure.Estimate;

public class LengthNormalizedEstimate<T> implements Estimate<TimeSeries<T>> {
  private Estimate<TimeSeries<T>> estimate;

  public LengthNormalizedEstimate(Estimate<TimeSeries<T>> estimate) {
    this.estimate = estimate;
  }

  public double estimate(TimeSeries<T> ts) {
    return this.estimate.estimate(ts) / (ts.size() - 1);
  }
}
