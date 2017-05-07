package swf.evaluation.slidingwindow;

import java.util.List;
import swf.Accel;
import swf.TimeSeries;
import swf.measure.Distance;

public interface Threshold {
  public double[] threshold(
      List<TimeSeries<Accel>> tsList,
      Distance<TimeSeries<Accel>> distance
  );
}
