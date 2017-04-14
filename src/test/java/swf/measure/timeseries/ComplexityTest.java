import static org.junit.Assert.assertEquals;

import org.junit.Test;
import swf.TimeSeries;
import swf.measure.Distance;
import swf.measure.timeseries.Complexity;
import swf.timeseries.Point;

public class ComplexityTest {
  @Test
  public void testEstimate() {
    TimeSeries<Integer> ts = new TimeSeries<Integer>();
    ts.add(new Point<Integer>(-47));
    ts.add(new Point<Integer>(-35));
    ts.add(new Point<Integer>(-65));
    ts.add(new Point<Integer>(-47));
    Distance<Integer> distance = new Distance<Integer>() {
      public double distance(Integer int1, Integer int2) {
        return Math.abs(int1 - int2);
      }
    };
    assertEquals(Math.sqrt(1368), new Complexity<Integer>(distance).estimate(ts), 0);
  }
}
