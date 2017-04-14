import static org.junit.Assert.assertEquals;

import org.junit.Test;
import swf.measure.Distance;
import swf.measure.MultiplyDistance;

public class MultiplyDistanceTest {
  @Test
  public void testDistance() {
    assertEquals(
        4394,
        new MultiplyDistance<Integer>(
            new Distance<Integer>() {
              public double distance(Integer int1, Integer int2) {
                return 2 * Math.abs(int1 - int2);
              }
            },
            new Distance<Integer>() {
              public double distance(Integer int1, Integer int2) {
                int dist = Math.abs(int1 - int2);
                return dist * dist;
              }
            }
        ).distance(-69, -56),
        0
    );
  }
}
