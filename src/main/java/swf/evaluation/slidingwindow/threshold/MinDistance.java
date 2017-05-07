package swf.evaluation.slidingwindow.threshold;

import java.util.List;
import swf.Accel;
import swf.TimeSeries;
import swf.evaluation.slidingwindow.Threshold;
import swf.measure.Distance;

public class MinDistance implements Threshold {
  /**
   * Calculates the minimum distance between the TimeSeries.
   */
  public double[] threshold(
      List<TimeSeries<Accel>> tsList,
      Distance<TimeSeries<Accel>> distance
  ) {
    double[] thresholds = new double[tsList.size()];
    int index = 0;
    for (TimeSeries<Accel> ts1 : tsList) {
      double min = Double.MAX_VALUE;
      for (TimeSeries<Accel> ts2 : tsList) {
        if (ts1 != ts2) {
          double dist = distance.distance(ts1, ts2);
          if (dist < min) {
            min = dist;
          }
        }
      }
      thresholds[index] = min;
      index++;
    }
    return thresholds;
  }
}
