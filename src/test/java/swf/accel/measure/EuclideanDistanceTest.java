import static org.junit.Assert.assertEquals;

import org.junit.Test;
import swf.Accel;
import swf.accel.measure.EuclideanDistance;

public class EuclideanDistanceTest {
  @Test
  public void testDistance() {
    assertEquals(
        Math.sqrt(20606),
        new EuclideanDistance().distance(new Accel(-13, 84, -58), new Accel(-4, -58, -77)),
        0
    );
  }
}
