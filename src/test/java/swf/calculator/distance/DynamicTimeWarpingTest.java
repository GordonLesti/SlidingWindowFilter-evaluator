import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import org.junit.Before;
import org.junit.Test;
import swf.calculator.Distance;
import swf.calculator.distance.DynamicTimeWarping;
import swf.model.TimeSeries;
import swf.model.timeseries.Item;

public class DynamicTimeWarpingTest {
  private DynamicTimeWarping<Integer> dtw;

  /**
   * Creates DynamicTimeWarping for Integer values.
   */
  @Before
  public void oneTimeSetUp() {
    this.dtw = new DynamicTimeWarping<Integer>(
        new Distance<Double, Integer>() {
          public Double calculateDistance(Integer num1, Integer num2) {
            return Math.abs(num1 - num2) * 1.0;
          }
        }
    );
  }

  @Test
  public void testCalculateDistanceSameSize() {
    int[] intArray1 = {83, 65, 80, 49, 87};
    TimeSeries<Integer> timeSeries1 = this.createTimeSeriesFromIntArray(intArray1);
    int[] intArray2 = {57, 99, 11, 75, 78};
    TimeSeries<Integer> timeSeries2 = this.createTimeSeriesFromIntArray(intArray2);
    assertEquals(112.0, this.dtw.calculateDistance(timeSeries1, timeSeries2), 0);
  }

  @Test
  public void testCalculateDistanceDiffSize() {
    int[] intArray1 = {17, 72, 50, 9};
    TimeSeries<Integer> timeSeries1 = this.createTimeSeriesFromIntArray(intArray1);
    int[] intArray2 = {21, 43, 13, 23, 40};
    TimeSeries<Integer> timeSeries2 = this.createTimeSeriesFromIntArray(intArray2);
    assertEquals(89.0, this.dtw.calculateDistance(timeSeries1, timeSeries2), 0);
  }

  private static TimeSeries<Integer> createTimeSeriesFromIntArray(int[] intArray) {
    LinkedList<Item<Integer>> itemList = new LinkedList<Item<Integer>>();
    for (int i = 0; i < intArray.length; i++) {
      itemList.add(new Item<Integer>(0, intArray[i]));
    }
    return new TimeSeries<Integer>(itemList, new LinkedList<Item<String>>());
  }
}
