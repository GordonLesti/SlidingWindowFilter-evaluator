package swf.measure.timeseries;

import swf.TimeSeries;
import swf.measure.Distance;
import swf.measure.Estimate;
import swf.operator.Add;
import swf.operator.ScalarMult;
import swf.timeseries.Point;

public class Variance<T> implements Estimate<TimeSeries<T>> {
  private Distance<T> distance;
  private Add<T> add;
  private ScalarMult<T> mult;

  /**
   * Creates a variance calculator for timeseries.
   */
  public Variance(Distance<T> distance, Add<T> add, ScalarMult<T> mult) {
    this.distance = distance;
    this.add = add;
    this.mult = mult;
  }

  /**
   * Calculates the variance for a timeseries.
   */
  public double estimate(TimeSeries<T> ts) {
    T sumData = null;
    for (Point<T> point : ts) {
      T curr = point.getData();
      if (sumData == null) {
        sumData = curr;
      } else {
        sumData = this.add.add(sumData, curr);
      }
    }
    int size = ts.size();
    T mean = this.mult.mult(1.0 / size, sumData);
    double sum = 0;
    for (Point<T> point : ts) {
      double dist = this.distance.distance(point.getData(), mean);
      sum += dist * dist;
    }
    return sum / (size - 1);
  }
}
