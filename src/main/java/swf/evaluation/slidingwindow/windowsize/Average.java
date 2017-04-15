package swf.evaluation.slidingwindow.windowsize;

import java.util.Collection;
import swf.Accel;
import swf.TimeSeries;
import swf.evaluation.slidingwindow.WindowSize;

public class Average implements WindowSize {
  /**
   * Returns the minimum size of all TimeSeries.
   */
  public int windowSize(Collection<TimeSeries<Accel>> tsCollection) {
    int sum = 0;
    for (TimeSeries<Accel> ts : tsCollection) {
      sum += ts.size();
    }
    return (int) Math.ceil((sum * 1.0) / tsCollection.size());
  }
}
