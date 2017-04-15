package swf.evaluation.slidingwindowfilter;

import java.util.Collection;
import swf.Accel;
import swf.TimeSeries;

public interface WindowSize {
  public int windowSize(Collection<TimeSeries<Accel>> tsCollection);
}
