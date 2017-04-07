import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import swf.calculator.Distance;
import swf.calculator.Subtraction;
import swf.calculator.distance.DynamicTimeWarping;
import swf.calculator.distance.NormalizeDynamicTimeWarping;
import swf.calculator.measure.Mean;
import swf.model.TimeSeries;
import swf.model.timeseries.Item;
import swf.transformer.NormalizeTransformer;

public class NormalizeDynamicTimeWarpingTest {
  @Test
  public void testCalculateDistance() {
    DynamicTimeWarping<Integer> dtw = new DynamicTimeWarping<Integer>(
        new Distance<Double, Integer>() {
          public Double calculateDistance(Integer num1, Integer num2) {
            return Math.abs(num1 - num2) * 1.0;
          }
        }
    );
    NormalizeTransformer<Integer> normalizeTransformer = new NormalizeTransformer<Integer>(
        new Subtraction<Integer>() {
          public Integer subtract(Integer num1, Integer num2) {
            return num1 - num2;
          }
        },
        new Mean<Integer>() {
          public Integer calculate(List<Integer> list) {
            int sum = 0;
            Iterator<Integer> iterator = list.iterator();
            while (iterator.hasNext()) {
              sum += iterator.next();
            }
            return sum / list.size();
          }
        }
    );
    NormalizeDynamicTimeWarping<Integer> normalizeDtw =
        new NormalizeDynamicTimeWarping<Integer>(
            dtw,
            normalizeTransformer
        );
    int[] intArray1 = {5, 2, 1, 4};
    int[] intArray2 = {0, 9, 1, 6};
    int[] intArray3 = {2, -1, -2, 1};
    int[] intArray4 = {-4, 5, -3, 2};
    assertEquals(
        dtw.calculateDistance(
            createTimeSeriesFromIntArray(intArray3),
            createTimeSeriesFromIntArray(intArray4)
        ),
        normalizeDtw.calculateDistance(
            createTimeSeriesFromIntArray(intArray1),
            createTimeSeriesFromIntArray(intArray2)
        )
    );
  }

  private static TimeSeries<Integer> createTimeSeriesFromIntArray(int[] intArray) {
    LinkedList<Item<Integer>> itemList = new LinkedList<Item<Integer>>();
    for (int i = 0; i < intArray.length; i++) {
      itemList.add(new Item<Integer>(0, intArray[i]));
    }
    return new TimeSeries<Integer>(itemList, new LinkedList<Item<String>>());
  }
}
