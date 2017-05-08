import static org.junit.Assert.assertArrayEquals;

import java.util.LinkedList;
import org.junit.Test;
import swf.Accel;
import swf.TimeSeries;
import swf.evaluation.slidingwindow.threshold.HalfAverageDistance;
import swf.measure.Distance;
import swf.timeseries.Point;

public class HalfAverageDistanceTest {
  @Test
  public void testThreshold() {
    int[] sizes = {34, 69, 9, 71};
    LinkedList<TimeSeries<Accel>> tsList = new LinkedList<TimeSeries<Accel>>();
    for (int i = 0; i < sizes.length; i++) {
      TimeSeries<Accel> ts = new TimeSeries<Accel>();
      for (int j = 0; j < sizes[i]; j++) {
        ts.add(new Point<Accel>(new Accel(-5, 41, -66), "IJAaqEGI"));
      }
      tsList.add(ts);
    }
    double[] thresholds = {97.0 / 6, 97.0 / 6, 147.0 / 6, 101.0 / 6};
    assertArrayEquals(
        thresholds,
        new HalfAverageDistance().threshold(
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
