import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import org.junit.Test;
import swf.Accel;
import swf.TimeSeries;
import swf.evaluation.slidingwindowfilter.windowsize.Average;
import swf.timeseries.Point;

public class AverageTest {
  @Test
  public void testWindowSize() {
    int[] sizes = {76, 57, 71, 74};
    LinkedList<TimeSeries<Accel>> tsList = new LinkedList<TimeSeries<Accel>>();
    for (int i = 0; i < sizes.length; i++) {
      TimeSeries<Accel> ts = new TimeSeries<Accel>();
      for (int j = 0; j < sizes[i]; j++) {
        ts.add(new Point<Accel>(new Accel(-44, -50, -68)));
      }
      tsList.add(ts);
    }
    assertEquals(70, new Average().windowSize(tsList));
  }
}
