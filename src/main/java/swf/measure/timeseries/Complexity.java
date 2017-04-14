package swf.measure.timeseries;

import swf.TimeSeries;
import swf.measure.Distance;
import swf.measure.Estimate;
import swf.timeseries.Point;

public class Complexity<T> implements Estimate<TimeSeries<T>> {
  private Distance<T> distance;

  public Complexity(Distance<T> distance) {
    this.distance = distance;
  }

  /**
   * Calculates the complexity of a time series.
   */
  public double estimate(TimeSeries<T> ts) {
    T pre = null;
    double complexity = 0;
    for (Point<T> point : ts) {
      T curr = point.getData();
      if (pre != null) {
        double dist = this.distance.distance(pre, curr);
        complexity += dist * dist;
      }
      pre = curr;
    }
    return Math.sqrt(complexity);
  }
}
