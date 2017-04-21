import static org.junit.Assert.assertEquals;

import org.junit.Test;
import swf.TimeSeries;
import swf.measure.Estimate;
import swf.measure.timeseries.AverageEstimate;
import swf.timeseries.Point;

public class AverageEstimateTest {
  @Test
  public void testEstimate() {
    TimeSeries<Integer> intTs = new TimeSeries<Integer>();
    for (int i = 0; i < 91; i++) {
      intTs.add(new Point<Integer>(-19, "3P2wRadM"));
    }
    Estimate<TimeSeries<Integer>> estimate = new Estimate<TimeSeries<Integer>>() {
      public double estimate(TimeSeries<Integer> ts) {
        return (ts.size() * ts.size()) / 2.0;
      }
    };
    assertEquals(8281.0 / 180, new AverageEstimate<Integer>(estimate).estimate(intTs), 0);
  }
}
