package swf.timeseries.normalizer;

import swf.TimeSeries;
import swf.measure.Distance;
import swf.operator.Add;
import swf.operator.ScalarMult;
import swf.timeseries.Normalizer;
import swf.timeseries.Point;

public class ZeroMeanOneVariance<T> implements Normalizer<T> {
  private Distance<T> distance;
  private Add<T> add;
  private ScalarMult<T> mult;

  /**
   * Constructor for ZeroMeanOneVariance.
   */
  public ZeroMeanOneVariance(Distance<T> distance, Add<T> add, ScalarMult<T> mult) {
    this.distance = distance;
    this.add = add;
    this.mult = mult;
  }

  /**
   * Subtracts the mean from every point in the time series and divides the deviation.
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
    T mean = this.mult.mult(1.0 / ts.size(), sum);
    double deviationSum = 0;
    for (Point<T> point : ts) {
      double dist = this.distance.distance(point.getData(), mean);
      deviationSum += dist * dist;
    }
    double deviationFactor = 1 / Math.sqrt(deviationSum / (ts.size() - 1));
    T invMean = this.mult.mult(-1.0, mean);
    TimeSeries<T> normTs = new TimeSeries<T>();
    for (Point<T> point : ts) {
      T data = point.getData();
      normTs.add(
          new Point<T>(
              this.mult.mult(deviationFactor, this.add.add(data, invMean)),
              point.getTag()
          )
      );
    }
    return normTs;
  }
}
