import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import org.junit.Test;
import swf.Accel;
import swf.TimeSeries;
import swf.evaluation.slidingwindow.windowsize.Max;
import swf.timeseries.Point;

public class MaxTest {
  @Test
  public void testWindowSize() {
    int[] sizes = {63, 99, 12};
    LinkedList<TimeSeries<Accel>> tsList = new LinkedList<TimeSeries<Accel>>();
    for (int i = 0; i < sizes.length; i++) {
      TimeSeries<Accel> ts = new TimeSeries<Accel>();
      for (int j = 0; j < sizes[i]; j++) {
        ts.add(new Point<Accel>(new Accel(-89, -20, -8)));
      }
      tsList.add(ts);
    }
    assertEquals(99, new Max().windowSize(tsList));
  }
}
