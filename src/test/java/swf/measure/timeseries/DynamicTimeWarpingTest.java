import static org.junit.Assert.assertEquals;

import org.junit.Test;
import swf.TimeSeries;
import swf.measure.Distance;
import swf.measure.timeseries.DynamicTimeWarping;
import swf.timeseries.Point;

public class DynamicTimeWarpingTest {
  @Test
  public void testDistance() {
    TimeSeries<Integer> ts1 = new TimeSeries<Integer>();
    ts1.add(new Point<Integer>(-88));
    ts1.add(new Point<Integer>(14));
    ts1.add(new Point<Integer>(95));
    TimeSeries<Integer> ts2 = new TimeSeries<Integer>();
    ts2.add(new Point<Integer>(95));
    ts2.add(new Point<Integer>(-23));
    ts2.add(new Point<Integer>(89));
    ts2.add(new Point<Integer>(68));
    Distance<Integer> distance = new Distance<Integer>() {
      public double distance(Integer int1, Integer int2) {
        return Math.abs(int1 - int2) * 1.0;
      }
    };
    assertEquals(253, new DynamicTimeWarping<Integer>(distance).distance(ts1, ts2), 0);
  }
}
