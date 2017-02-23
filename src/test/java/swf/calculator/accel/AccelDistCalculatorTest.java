import static org.junit.Assert.assertEquals;

import org.junit.Test;
import swf.calculator.accel.AccelDistCalculator;
import swf.model.AccelerationData;

public class AccelDistCalculatorTest {
  @Test
  public void testCalculateDistance() {
    AccelDistCalculator accelDistCalculator = new AccelDistCalculator();
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
