package swf.evaluation.slidingwindow.threshold;

import java.util.Collection;
import swf.Accel;
import swf.TimeSeries;
import swf.evaluation.slidingwindow.Threshold;
import swf.measure.Distance;

public class MinDistance implements Threshold {
  /**
   * Calculates the minimum distance between the TimeSeries.
   */
  public double threshold(
      Collection<TimeSeries<Accel>> tsCollection,
      Distance<TimeSeries<Accel>> distance
  ) {
    double min = Double.MAX_VALUE;
    for (TimeSeries<Accel> ts1 : tsCollection) {
      for (TimeSeries<Accel> ts2 : tsCollection) {
        if (ts1 != ts2) {
          double dist = distance.distance(ts1, ts2);
          if (dist < min) {
            min = dist;
          }
        }
      }
    }
    return min;
  }
}
