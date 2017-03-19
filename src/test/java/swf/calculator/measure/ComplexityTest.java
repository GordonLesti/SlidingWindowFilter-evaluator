import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import org.junit.Test;
import swf.calculator.Distance;
import swf.calculator.measure.Complexity;
import swf.model.TimeSeries;
import swf.model.timeseries.Item;

public class ComplexityTest {
  @Test
  public void testCalculate() {
    LinkedList<Item<Integer>> itemList = new LinkedList<Item<Integer>>();
    itemList.add(new Item<Integer>(0, 1));
    itemList.add(new Item<Integer>(0, 5));
    itemList.add(new Item<Integer>(0, -7));
    itemList.add(new Item<Integer>(0, 7));
    itemList.add(new Item<Integer>(0, 3));
    Complexity<Integer> complexityCalculator =
        new Complexity<Integer>(new Distance<Double, Integer>() {
          public Double calculateDistance(Integer int1, Integer int2) {
            return 1.0 * Math.abs(int1 - int2);
          }
        });
    assertEquals(
        Math.sqrt(372),
        complexityCalculator.calculate(
            new TimeSeries<Integer>(itemList, new LinkedList<Item<String>>())
        ),
        0
    );
  }
}
