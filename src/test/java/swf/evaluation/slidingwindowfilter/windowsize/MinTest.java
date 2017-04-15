import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import org.junit.Test;
import swf.Accel;
import swf.TimeSeries;
import swf.evaluation.slidingwindowfilter.windowsize.Min;
import swf.timeseries.Point;

public class MinTest {
  @Test
  public void testWindowSize() {
    int[] sizes = {16, 19, 32};
    LinkedList<TimeSeries<Accel>> tsList = new LinkedList<TimeSeries<Accel>>();
    for (int i = 0; i < sizes.length; i++) {
      TimeSeries<Accel> ts = new TimeSeries<Accel>();
      for (int j = 0; j < sizes[i]; j++) {
        ts.add(new Point<Accel>(new Accel(-41, -71, 32)));
      }
      tsList.add(ts);
    }
    assertEquals(16, new Min().windowSize(tsList));
  }
}
