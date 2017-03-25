import static org.junit.Assert.assertEquals;

import org.junit.Test;
import swf.calculator.Measure;
import swf.calculator.distance.MaxMinQuotient;

public class MaxMinQuotientTest {
  @Test
  public void testCalculateDistance() {
    Measure<Double, Integer> measure = new Measure<Double, Integer>() {
      public Double calculate(Integer obj) {
        return new Double(obj * obj);
      }
    };
    MaxMinQuotient<Integer> maxMinQuotient = new MaxMinQuotient<Integer>(measure);
    assertEquals(36.0 / 25.0, maxMinQuotient.calculateDistance(5, 6), 0);
  }
}
