import static org.junit.Assert.assertEquals;

import org.junit.Test;
import swf.TimeSeries;
import swf.measure.Distance;
import swf.measure.timeseries.Variance;
import swf.operator.Add;
import swf.operator.ScalarMult;
import swf.timeseries.Point;

public class VarianceTest {
  @Test
  public void testEstmate() {
    TimeSeries<Integer> ts = new TimeSeries<Integer>();
    ts.add(new Point<Integer>(33));
    ts.add(new Point<Integer>(-51));
    ts.add(new Point<Integer>(-79));
    ts.add(new Point<Integer>(84));
    Distance<Integer> distance = new Distance<Integer>() {
      public double distance(Integer int1, Integer int2) {
        return Math.abs(int1 - int2);
      }
    };
    Add<Integer> add = new Add<Integer>() {
      public Integer add(Integer int1, Integer int2) {
        return int1 + int2;
      }
    };
    ScalarMult<Integer> mult = new ScalarMult<Integer>() {
      public Integer mult(double scalar, Integer num) {
        return (int) Math.round(scalar * num);
      }
    };
    assertEquals(16945.0 / 3, new Variance<Integer>(distance, add, mult).estimate(ts), 0);
  }
}
