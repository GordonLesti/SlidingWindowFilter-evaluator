import static org.junit.Assert.assertArrayEquals;

import java.util.LinkedList;
import org.junit.Test;
import swf.Accel;
import swf.TimeSeries;
import swf.evaluation.slidingwindow.threshold.HalfMiddleDistance;
import swf.measure.Distance;
import swf.timeseries.Point;

public class HalfMiddleDistanceTest {
  @Test
  public void testThreshold() {
    int[] sizes = {26, 66, 23, 17};
    LinkedList<TimeSeries<Accel>> tsList = new LinkedList<TimeSeries<Accel>>();
    for (int i = 0; i < sizes.length; i++) {
      TimeSeries<Accel> ts = new TimeSeries<Accel>();
      for (int j = 0; j < sizes[i]; j++) {
        ts.add(new Point<Accel>(new Accel(-40, 39, -91), "IEgLoRp7"));
      }
      tsList.add(ts);
    }
    double[] thresholds = {43.0 / 4, 89.0 / 4, 46.0 / 4, 55.0 / 4};
    assertArrayEquals(
        thresholds,
        new HalfMiddleDistance().threshold(
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
