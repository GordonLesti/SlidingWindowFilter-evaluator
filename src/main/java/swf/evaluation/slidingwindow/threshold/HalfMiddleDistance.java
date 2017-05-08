package swf.evaluation.slidingwindow.threshold;

import java.util.List;
import swf.Accel;
import swf.TimeSeries;
import swf.evaluation.slidingwindow.Threshold;
import swf.measure.Distance;

public class HalfMiddleDistance implements Threshold {
  /**
   * Calculates the half middle distance between the TimeSeries.
   */
  public double[] threshold(
      List<TimeSeries<Accel>> trainingList,
      List<TimeSeries<Accel>> testList,
      Distance<TimeSeries<Accel>> distance
  ) {
    double[] thresholds = new double[trainingList.size()];
    int index = 0;
    for (TimeSeries<Accel> ts1 : trainingList) {
      double min = Double.MAX_VALUE;
      double max = Double.MIN_VALUE;
      for (TimeSeries<Accel> ts2 : trainingList) {
        if (ts1 != ts2) {
          double dist = distance.distance(ts1, ts2);
          if (dist < min) {
            min = dist;
          }
          if (dist > max) {
            max = dist;
          }
        }
      }
      thresholds[index] = (min + max) / 4;
      index++;
    }
    return thresholds;
  }
}
