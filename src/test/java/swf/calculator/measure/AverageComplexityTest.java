import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import org.junit.Test;
import swf.calculator.Distance;
import swf.calculator.measure.AverageComplexity;
import swf.calculator.measure.Complexity;
import swf.model.TimeSeries;
import swf.model.timeseries.Item;

public class AverageComplexityTest {
  @Test
  public void testCalculate() {
    LinkedList<Item<Integer>> itemList = new LinkedList<Item<Integer>>();
    itemList.add(new Item<Integer>(0, -54));
    itemList.add(new Item<Integer>(0, 13));
    itemList.add(new Item<Integer>(0, 90));
    itemList.add(new Item<Integer>(0, 58));
    itemList.add(new Item<Integer>(0, 64));
    Complexity<Integer> complexityCalculator =
        new Complexity<Integer>(new Distance<Double, Integer>() {
          public Double calculateDistance(Integer int1, Integer int2) {
            return 1.0 * Math.abs(int1 - int2);
          }
        });
    AverageComplexity<Integer> averageComplexityCalculator =
        new AverageComplexity<Integer>(complexityCalculator);
    TimeSeries<Integer> timeSeries =
        new TimeSeries<Integer>(itemList, new LinkedList<Item<String>>());
    assertEquals(
        complexityCalculator.calculate(timeSeries) / 5,
        averageComplexityCalculator.calculate(timeSeries),
        0
    );
  }
}
