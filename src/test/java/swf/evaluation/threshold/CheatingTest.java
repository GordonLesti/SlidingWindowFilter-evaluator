import static org.junit.Assert.assertArrayEquals;

import java.util.LinkedList;
import org.junit.Test;
import swf.Accel;
import swf.TimeSeries;
import swf.evaluation.slidingwindow.threshold.Cheating;
import swf.measure.Distance;
import swf.timeseries.Point;

public class CheatingTest {
  @Test
  public void testThreshold() {
    int[] trainingSizes = {18, 94, 55, 6};
    LinkedList<TimeSeries<Accel>> trainingList = new LinkedList<TimeSeries<Accel>>();
    for (int i = 0; i < trainingSizes.length; i++) {
      TimeSeries<Accel> ts = new TimeSeries<Accel>();
      for (int j = 0; j < trainingSizes[i]; j++) {
        ts.add(new Point<Accel>(new Accel(-38, -50, 84), "LqAqbk3A"));
      }
      trainingList.add(ts);
    }
    int[] testSizes = {83, 51, 52, 56};
    LinkedList<TimeSeries<Accel>> testList = new LinkedList<TimeSeries<Accel>>();
    for (int i = 0; i < testSizes.length; i++) {
      TimeSeries<Accel> ts = new TimeSeries<Accel>();
      for (int j = 0; j < testSizes[i]; j++) {
        ts.add(new Point<Accel>(new Accel(0, 82, 9), "J7kyKgWm"));
      }
      testList.add(ts);
    }
    double[] thresholds = {65 * 1.1, 43 * 1.1, 3 * 1.1, 50 * 1.1};
    assertArrayEquals(
        thresholds,
        new Cheating(1.1).threshold(
            trainingList,
            testList,
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
