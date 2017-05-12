package swf.evaluation.slidingwindow.threshold;

import java.util.List;
import swf.Accel;
import swf.TimeSeries;
import swf.evaluation.slidingwindow.Threshold;
import swf.measure.Distance;

public class HalfMinDistance implements Threshold {
  /**
   * Calculates the half minimum distance between the TimeSeries.
   */
  public double[] threshold(
      List<TimeSeries<Accel>> trainingList,
      List<TimeSeries<Accel>> testList,
      Distance<TimeSeries<Accel>> distance
  ) {
    double[] thresholds = new double[trainingList.size()];
    int index = 0;
    for (TimeSeries<Accel> ts1 : trainingList) {
      double min = Double.POSITIVE_INFINITY;
      for (TimeSeries<Accel> ts2 : trainingList) {
        if (ts1 != ts2) {
          double dist = distance.distance(ts1, ts2);
          if (dist < min) {
            min = dist;
          }
        }
      }
      thresholds[index] = min / 2;
      index++;
    }
    return thresholds;
  }
}
