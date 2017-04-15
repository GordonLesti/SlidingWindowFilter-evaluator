package swf.evaluation.slidingwindow.windowsize;

import java.util.Collection;
import swf.Accel;
import swf.TimeSeries;
import swf.evaluation.slidingwindow.WindowSize;

public class Max implements WindowSize {
  /**
   * Returns the maximum size of all TimeSeries.
   */
  public int windowSize(Collection<TimeSeries<Accel>> tsCollection) {
    int max = Integer.MIN_VALUE;
    for (TimeSeries<Accel> ts : tsCollection) {
      int size = ts.size();
      if (size > max) {
        max = size;
      }
    }
    return max;
  }
}
