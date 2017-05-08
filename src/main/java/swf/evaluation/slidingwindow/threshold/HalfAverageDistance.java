package swf.evaluation.slidingwindow.threshold;

import java.util.List;
import swf.Accel;
import swf.TimeSeries;
import swf.evaluation.slidingwindow.Threshold;
import swf.measure.Distance;

public class HalfAverageDistance implements Threshold {
  /**
   * Calculates the half average distance between the TimeSeries.
   */
  public double[] threshold(
      List<TimeSeries<Accel>> trainingList,
      List<TimeSeries<Accel>> testList,
      Distance<TimeSeries<Accel>> distance
  ) {
    double[] thresholds = new double[trainingList.size()];
    int index = 0;
    for (TimeSeries<Accel> ts1 : trainingList) {
      double sum = 0;
      for (TimeSeries<Accel> ts2 : trainingList) {
        if (ts1 != ts2) {
          sum += distance.distance(ts1, ts2);
        }
      }
      thresholds[index] = sum / (2 * (trainingList.size() - 1));
      index++;
    }
    return thresholds;
  }
}
