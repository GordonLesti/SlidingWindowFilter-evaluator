package swf.evaluation.slidingwindow;

import java.util.Collection;
import swf.Accel;
import swf.TimeSeries;
import swf.measure.Distance;

public interface Threshold {
  public double threshold(
      Collection<TimeSeries<Accel>> tsCollection,
      Distance<TimeSeries<Accel>> distance
  );
}
