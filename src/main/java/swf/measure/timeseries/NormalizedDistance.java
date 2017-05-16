package swf.measure.timeseries;

import swf.TimeSeries;
import swf.measure.Distance;
import swf.operator.Add;
import swf.operator.ScalarMult;
import swf.timeseries.Point;

public class NormalizedDistance<T> implements Distance<TimeSeries<T>> {
  private Distance<TimeSeries<T>> distance;
  private Add<T> add;
  private ScalarMult<T> mult;

  /**
   * Creates a NormalizedDistance that normalizes time series before calculating distance.
   */
  public NormalizedDistance(Distance<TimeSeries<T>> distance, Add<T> add, ScalarMult<T> mult) {
    this.distance = distance;
    this.add = add;
    this.mult = mult;
  }

  /**
   * Normalizes time series and calculate the distance afterwards.
   */
  public double distance(TimeSeries<T> ts1, TimeSeries<T> ts2) {
    return this.distance.distance(
        this.normalizeTimeSeries(ts1),
        this.normalizeTimeSeries(ts2)
    );
  }

  private TimeSeries<T> normalizeTimeSeries(TimeSeries<T> ts) {
    T sum = null;
    for (Point<T> point : ts) {
      T curr = point.getData();
      if (sum == null) {
        sum = curr;
      } else {
        sum = this.add.add(sum, curr);
      }
    }
    T invMean = this.mult.mult(-1.0 / ts.size(), sum);
    TimeSeries<T> normTs = new TimeSeries<T>();
    for (Point<T> point : ts) {
      T data = point.getData();
      normTs.add(new Point<T>(this.add.add(data, invMean), point.getTag()));
    }
    return normTs;
  }
}
