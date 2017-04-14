import static org.junit.Assert.assertEquals;

import org.junit.Test;
import swf.Accel;
import swf.TimeSeries;
import swf.accel.io.TimeSeriesParser;
import swf.timeseries.Point;

public class TimeSeriesParserTest {
  @Test
  public void testParseFile() {
    TimeSeries<Accel> ts = new TimeSeries<Accel>();
    ts.add(new Point<Accel>(new Accel(-4, -2, 13)));
    ts.add(new Point<Accel>(new Accel(-4, -2, 12)));
    ts.add(new Point<Accel>(new Accel(-5, -2, 12)));
    ts.add(new Point<Accel>(new Accel(-5, -2, 11)));
    ts.add(new Point<Accel>(new Accel(-5, -2, 11)));
    ts.add(new Point<Accel>(new Accel(-6, -2, 11)));
    ts.add(new Point<Accel>(new Accel(-6, -3, 10)));
    ts.add(new Point<Accel>(new Accel(-6, -3, 9)));
    ts.add(new Point<Accel>(new Accel(-5, -3, 8)));
    ts.add(new Point<Accel>(new Accel(-5, -3, 7), "1"));
    ts.add(new Point<Accel>(new Accel(-5, -3, 6), "1"));
    ts.add(new Point<Accel>(new Accel(-4, -3, 6), "1"));
    ts.add(new Point<Accel>(new Accel(-2, -3, 5), "1"));
    ts.add(new Point<Accel>(new Accel(-2, -3, 5), "1"));
    ts.add(new Point<Accel>(new Accel(-2, -4, 6), "1"));
    ts.add(new Point<Accel>(new Accel(-1, -4, 5), "1"));
    ts.add(new Point<Accel>(new Accel(0, -3, 5), "1"));
    ts.add(new Point<Accel>(new Accel(1, -3, 6), "1"));
    ts.add(new Point<Accel>(new Accel(1, -3, 6), "1"));
    ts.add(new Point<Accel>(new Accel(1, -3, 6)));
    ts.add(new Point<Accel>(new Accel(2, -3, 7)));
    ts.add(new Point<Accel>(new Accel(2, -3, 7)));
    ts.add(new Point<Accel>(new Accel(2, -3, 7)));
    ts.add(new Point<Accel>(new Accel(2, -3, 8)));
    ts.add(new Point<Accel>(new Accel(3, -2, 8)));
    ts.add(new Point<Accel>(new Accel(2, -2, 9)));
    ts.add(new Point<Accel>(new Accel(2, -3, 10)));
    ts.add(new Point<Accel>(new Accel(3, -2, 10)));
    ts.add(new Point<Accel>(new Accel(3, -2, 11)));
    ts.add(new Point<Accel>(new Accel(2, -2, 11)));
    assertEquals(ts, new TimeSeriesParser().parseFile("build/resources/test/record1.txt"));
  }
}
