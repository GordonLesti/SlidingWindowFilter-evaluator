package swf.evaluation.slidingwindow.threshold;

import java.util.Collection;
import swf.Accel;
import swf.TimeSeries;
import swf.evaluation.slidingwindow.Threshold;
import swf.measure.Distance;

public class AverageDistance implements Threshold {
  /**
   * Calculates the average distance between the TimeSeries.
   */
  public double threshold(
      Collection<TimeSeries<Accel>> tsCollection,
      Distance<TimeSeries<Accel>> distance
  ) {
    double sum = 0;
    for (TimeSeries<Accel> ts1 : tsCollection) {
      for (TimeSeries<Accel> ts2 : tsCollection) {
        if (ts1 != ts2) {
          sum += distance.distance(ts1, ts2);
        }
      }
    }
    int size = tsCollection.size();
    return sum / (size * (size - 1));
  }
}
