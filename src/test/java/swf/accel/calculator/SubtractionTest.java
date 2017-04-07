import static org.junit.Assert.assertEquals;

import org.junit.Test;
import swf.accel.calculator.Subtraction;
import swf.accel.model.AccelerationData;

public class SubtractionTest {
  @Test
  public void testSubtract() {
    AccelerationData minuend = new AccelerationData(-2, 9, -2);
    AccelerationData subtrahend = new AccelerationData(-6, -3, 1);
    Subtraction subtraction = new Subtraction();
    assertEquals(new AccelerationData(4, 12, -3), subtraction.subtract(minuend, subtrahend));
  }
}
