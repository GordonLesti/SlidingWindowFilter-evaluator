import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import org.junit.Test;
import swf.measure.Distance;
import swf.nnc.FullSearch;

public class FullSearchTest {
  @Test
  public void testNearestNeighbour() {
    LinkedList<Integer> testData = new LinkedList<Integer>();
    testData.add(2);
    testData.add(63);
    testData.add(99);
    testData.add(61);
    testData.add(-20);
    testData.add(95);
    Distance<Integer> dist = new Distance<Integer>() {
      public double distance(Integer int1, Integer int2) {
        return Math.abs(int1 - int2) * 1.0;
      }
    };
    assertEquals(
        new Integer(2),
        new FullSearch<Integer>(dist, testData).nearestNeighbour(17)
    );
  }
}
