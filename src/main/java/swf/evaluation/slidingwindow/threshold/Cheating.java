package swf.evaluation.slidingwindow.threshold;

import java.util.List;
import swf.Accel;
import swf.TimeSeries;
import swf.evaluation.slidingwindow.Threshold;
import swf.measure.Distance;

public class Cheating implements Threshold {
  public double[] threshold(
      List<TimeSeries<Accel>> trainingList,
      List<TimeSeries<Accel>> testList,
      Distance<TimeSeries<Accel>> distance
  ) {
    double[] thresholds = new double[trainingList.size()];
    for (int i = 0; i < trainingList.size(); i++) {
      thresholds[i] = distance.distance(trainingList.get(i), testList.get(i)) * 1.1;
    }
    return thresholds;
  }
}
