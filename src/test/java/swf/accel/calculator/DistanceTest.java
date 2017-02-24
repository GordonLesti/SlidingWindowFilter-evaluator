import static org.junit.Assert.assertEquals;

import org.junit.Test;
import swf.accel.calculator.Distance;
import swf.accel.model.AccelerationData;

public class DistanceTest {
  @Test
  public void testCalculateDistance() {
    Distance accelDistCalculator = new Distance();
    assertEquals(
        Math.sqrt(50),
        accelDistCalculator.calculateDistance(
          new AccelerationData(6, 2, 0),
          new AccelerationData(3, 6, 5)
        ),
        0
    );
  }
}
