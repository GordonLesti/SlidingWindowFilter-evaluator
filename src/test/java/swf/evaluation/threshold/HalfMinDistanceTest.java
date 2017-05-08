import static org.junit.Assert.assertArrayEquals;

import java.util.LinkedList;
import org.junit.Test;
import swf.Accel;
import swf.TimeSeries;
import swf.evaluation.slidingwindow.threshold.HalfMinDistance;
import swf.measure.Distance;
import swf.timeseries.Point;

public class HalfMinDistanceTest {
  @Test
  public void testThreshold() {
    int[] sizes = {13, 68, 53};
    LinkedList<TimeSeries<Accel>> tsList = new LinkedList<TimeSeries<Accel>>();
    for (int i = 0; i < sizes.length; i++) {
      TimeSeries<Accel> ts = new TimeSeries<Accel>();
      for (int j = 0; j < sizes[i]; j++) {
        ts.add(new Point<Accel>(new Accel(69, 82, -37), "0rqpdwsg"));
      }
      tsList.add(ts);
    }
    double[] thresholds = {20, 7.5, 7.5};
    assertArrayEquals(
        thresholds,
        new HalfMinDistance().threshold(
            tsList,
            null,
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
