import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import org.junit.Test;
import swf.filter.Estimate;

public class EstimateTest {
  @Test
  public void testFilter() {
    swf.measure.Estimate<Integer> estimate = new swf.measure.Estimate<Integer>() {
      public double estimate(Integer num) {
        return num * 2;
      }
    };
    LinkedList<Integer> list = new LinkedList<Integer>();
    list.add(29);
    list.add(-76);
    list.add(18);
    Estimate<Integer> filter = new Estimate<Integer>(estimate, list, 0.75);
    assertFalse(filter.filter(-50));
    assertTrue(filter.filter(-49));
    assertTrue(filter.filter(-5));
    assertTrue(filter.filter(2));
    assertFalse(filter.filter(3));
  }
}
