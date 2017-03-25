import static org.junit.Assert.assertEquals;

import org.junit.Test;
import swf.calculator.Distance;
import swf.calculator.distance.MultiplyDistance;

public class MultiplyDistanceTest {
  @Test
  public void testCalculateDistance() {
    Distance<Double, Integer> dist1 = new Distance<Double, Integer>() {
      public Double calculateDistance(Integer obj1, Integer obj2) {
        return new Double(Math.abs(obj1 - obj2));
      }
    };
    Distance<Double, Integer> dist2 = new Distance<Double, Integer>() {
      public Double calculateDistance(Integer obj1, Integer obj2) {
        return new Double(-Math.abs(obj1 - obj2));
      }
    };
    MultiplyDistance<Integer> multiplyDistance = new MultiplyDistance<Integer>(dist1, dist2);
    assertEquals(-36, multiplyDistance.calculateDistance(29, 35), 0);
  }
}
