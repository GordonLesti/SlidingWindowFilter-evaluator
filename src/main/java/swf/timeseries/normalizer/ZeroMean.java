package swf.timeseries.normalizer;

import swf.TimeSeries;
import swf.operator.Add;
import swf.operator.ScalarMult;
import swf.timeseries.Normalizer;
import swf.timeseries.Point;

public class ZeroMean<T> implements Normalizer<T> {
  private Add<T> add;
  private ScalarMult<T> mult;

  public ZeroMean(Add<T> add, ScalarMult<T> mult) {
    this.add = add;
    this.mult = mult;
  }

  /**
   * Subtracts the mean from every point in the time series.
   */
  public TimeSeries<T> normalize(TimeSeries<T> ts) {
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
