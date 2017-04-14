import static org.junit.Assert.assertEquals;

import org.junit.Test;
import swf.TimeSeries;
import swf.measure.Estimate;
import swf.measure.timeseries.MaxMinQuotient;
import swf.timeseries.Point;

public class MaxMinQuotientTest {
  @Test
  public void testDistance() {
    Estimate<TimeSeries<Integer>> estimate = new Estimate<TimeSeries<Integer>>() {
      public double estimate(TimeSeries<Integer> ts) {
        return ts.size();
      }
    };
    TimeSeries<Integer> ts1 = new TimeSeries<Integer>();
    for (int i = 0; i < 6; i++) {
      ts1.add(new Point<Integer>(0));
    }
    TimeSeries<Integer> ts2 = new TimeSeries<Integer>();
    for (int i = 0; i < 9; i++) {
      ts2.add(new Point<Integer>(0));
    }
    assertEquals(9.0 / 6, new MaxMinQuotient<Integer>(estimate).distance(ts1, ts2), 0);
  }
}
