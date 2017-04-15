package swf.evaluation.slidingwindow.windowsize;

import java.util.Collection;
import swf.Accel;
import swf.TimeSeries;
import swf.evaluation.slidingwindow.WindowSize;

public class Middle implements WindowSize {
  /**
   * Returns the middle size of all TimeSeries.
   */
  public int windowSize(Collection<TimeSeries<Accel>> tsCollection) {
    int max = Integer.MIN_VALUE;
    int min = Integer.MAX_VALUE;
    for (TimeSeries<Accel> ts : tsCollection) {
      int size = ts.size();
      if (size > max) {
        max = size;
      }
      if (size < min) {
        min = size;
      }
    }
    return (int) Math.round((max + min) / 2.0);
  }
}
