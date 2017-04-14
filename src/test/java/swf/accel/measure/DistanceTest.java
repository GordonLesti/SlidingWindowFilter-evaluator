import static org.junit.Assert.assertEquals;

import org.junit.Test;
import swf.Accel;
import swf.accel.measure.Distance;

public class DistanceTest {
  @Test
  public void testDistance() {
    assertEquals(
        Math.sqrt(20606),
        new Distance().distance(new Accel(-13, 84, -58), new Accel(-4, -58, -77)),
        0
    );
  }
}
