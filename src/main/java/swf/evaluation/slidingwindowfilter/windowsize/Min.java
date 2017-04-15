package swf.evaluation.slidingwindowfilter.windowsize;

import java.util.Collection;
import swf.Accel;
import swf.TimeSeries;
import swf.evaluation.slidingwindowfilter.WindowSize;

public class Min implements WindowSize {
  /**
   * Returns the minimum size of all TimeSeries.
   */
  public int windowSize(Collection<TimeSeries<Accel>> tsCollection) {
    int min = Integer.MAX_VALUE;
    for (TimeSeries<Accel> ts : tsCollection) {
      int size = ts.size();
      if (size < min) {
        min = size;
      }
    }
    return min;
  }
}
