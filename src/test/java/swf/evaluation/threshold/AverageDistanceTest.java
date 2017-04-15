import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import org.junit.Test;
import swf.Accel;
import swf.TimeSeries;
import swf.evaluation.slidingwindow.threshold.AverageDistance;
import swf.measure.Distance;
import swf.timeseries.Point;

public class AverageDistanceTest {
  @Test
  public void testThreshold() {
    int[] sizes = {70, 33, 73};
    LinkedList<TimeSeries<Accel>> tsList = new LinkedList<TimeSeries<Accel>>();
    for (int i = 0; i < sizes.length; i++) {
      TimeSeries<Accel> ts = new TimeSeries<Accel>();
      for (int j = 0; j < sizes[i]; j++) {
        ts.add(new Point<Accel>(new Accel(-82, -37, 19), "JARYyUEz"));
      }
      tsList.add(ts);
    }
    assertEquals(
        160.0 / 6,
        new AverageDistance().threshold(
            tsList,
            new Distance<TimeSeries<Accel>>() {
              public double distance(TimeSeries<Accel> ts1, TimeSeries<Accel> ts2) {
                return Math.abs(ts1.size() - ts2.size());
              }
            }
        ),
        0
    );
  }
}
