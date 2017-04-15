import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import org.junit.Test;
import swf.Accel;
import swf.TimeSeries;
import swf.evaluation.slidingwindow.windowsize.Middle;
import swf.timeseries.Point;

public class MiddleTest {
  @Test
  public void testWindowSize() {
    int[] sizes = {92, 37, 50};
    LinkedList<TimeSeries<Accel>> tsList = new LinkedList<TimeSeries<Accel>>();
    for (int i = 0; i < sizes.length; i++) {
      TimeSeries<Accel> ts = new TimeSeries<Accel>();
      for (int j = 0; j < sizes[i]; j++) {
        ts.add(new Point<Accel>(new Accel(-32, -63, 54)));
      }
      tsList.add(ts);
    }
    assertEquals(65, new Middle().windowSize(tsList));
  }
}
