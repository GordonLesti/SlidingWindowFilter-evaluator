import static org.junit.Assert.assertEquals;

import org.junit.Test;
import swf.TimeSeries;
import swf.timeseries.Point;

public class TimeSeriesTest {
  @Test
  public void testIntervalByTag() {
    TimeSeries<Integer> ts1 = new TimeSeries<Integer>();
    ts1.add(new Point<Integer>(99));
    ts1.add(new Point<Integer>(57, "dlIFf4ga"));
    ts1.add(new Point<Integer>(30, "dlIFf4ga"));
    ts1.add(new Point<Integer>(65));
    ts1.add(new Point<Integer>(-28, "SYIPqdHL"));
    TimeSeries<Integer> ts2 = new TimeSeries<Integer>();
    ts2.add(new Point<Integer>(57, "dlIFf4ga"));
    ts2.add(new Point<Integer>(30, "dlIFf4ga"));
    assertEquals(ts2, ts1.intervalByTag("dlIFf4ga"));
  }

  @Test
  public void testSubTimeSeries() {
    TimeSeries<Integer> ts1 = new TimeSeries<Integer>();
    ts1.add(new Point<Integer>(46, "g6aCC8je"));
    ts1.add(new Point<Integer>(-44, "6XnT0bb3"));
    ts1.add(new Point<Integer>(24, "zWeXXcbY"));
    ts1.add(new Point<Integer>(-1, "DrSJvEl4"));
    ts1.add(new Point<Integer>(-68, "kfzBqIWP"));
    ts1.add(new Point<Integer>(-36, "0rqpdwsg"));
    TimeSeries<Integer> ts2 = new TimeSeries<Integer>();
    ts2.add(new Point<Integer>(-44, "6XnT0bb3"));
    ts2.add(new Point<Integer>(24, "zWeXXcbY"));
    ts2.add(new Point<Integer>(-1, "DrSJvEl4"));
    assertEquals(ts2, ts1.subTimeSeries(1, 4));
  }
}
