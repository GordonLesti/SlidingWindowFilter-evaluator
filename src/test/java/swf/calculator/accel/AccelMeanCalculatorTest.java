import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import org.junit.Test;
import swf.calculator.accel.AccelMeanCalculator;
import swf.model.AccelerationData;

public class AccelMeanCalculatorTest {
  @Test
  public void testCalculateMean() {
    LinkedList<AccelerationData> list = new LinkedList<AccelerationData>();
    list.add(new AccelerationData(-4, -6, 5));
    list.add(new AccelerationData(9, -4, 3));
    list.add(new AccelerationData(-6, 5, 8));
    AccelMeanCalculator accelMeanCalculator = new AccelMeanCalculator();
    assertEquals(new AccelerationData(0, -2, 5), accelMeanCalculator.calculateMean(list));
  }
}
