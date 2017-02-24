import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import org.junit.Test;
import swf.accel.model.AccelerationData;
import swf.accel.transformer.QuantizeTransformer;
import swf.model.TimeSeries;
import swf.model.timeseries.Item;

public class QuantizeTransformerTest {
  @Test
  public void testTransform() {
    int[][] items = {
        {201, 200, 199},
        {181, 180, 179},
        {161, 160, 159},
        {141, 140, 139},
        {121, 120, 119},
        {101, 100, 99},
        {91, 90, 89},
        {81, 80, 79},
        {71, 70, 69},
        {61, 60, 59},
        {51, 50, 49},
        {41, 40, 39},
        {31, 30, 29},
        {21, 20, 19},
        {11, 10, 9},
        {1, 0, -1},
        {-11, -10, -9},
        {-21, -20, -19},
        {-31, -30, -29},
        {-41, -40, -39},
        {-51, -50, -49},
        {-61, -60, -59},
        {-71, -70, -69},
        {-81, -80, -79},
        {-91, -90, -89},
        {-101, -100, -99},
        {-121, -120, -119},
        {-141, -140, -139},
        {-161, -160, -159},
        {-181, -180, -179},
        {-201, -200, -199}
    };
    TimeSeries<AccelerationData> timeSeries =
        this.createTimeSeriesFromMultiDimIntArray(items);
    int[][] assertedItems = {
        {16, 16, 15},
        {15, 15, 14},
        {14, 14, 13},
        {13, 13, 12},
        {12, 12, 11},
        {11, 11, 10},
        {10, 10, 9},
        {9, 9, 8},
        {8, 8, 7},
        {7, 7, 6},
        {6, 6, 5},
        {5, 5, 4},
        {4, 4, 3},
        {3, 3, 2},
        {2, 2, 1},
        {1, 0, -1},
        {-2, -2, -1},
        {-3, -3, -2},
        {-4, -4, -3},
        {-5, -5, -4},
        {-6, -6, -5},
        {-7, -7, -6},
        {-8, -8, -7},
        {-9, -9, -8},
        {-10, -10, -9},
        {-11, -11, -10},
        {-12, -12, -11},
        {-13, -13, -12},
        {-14, -14, -13},
        {-15, -15, -14},
        {-16, -16, -15}
    };
    TimeSeries<AccelerationData> assertedTimeSeries =
        this.createTimeSeriesFromMultiDimIntArray(assertedItems);
    QuantizeTransformer quantizeTransformer = new QuantizeTransformer();
    assertEquals(
        this.createTimeSeriesFromMultiDimIntArray(assertedItems),
        quantizeTransformer.transform(timeSeries)
    );
  }

  private TimeSeries<AccelerationData> createTimeSeriesFromMultiDimIntArray(int[][] multDimArray) {
    LinkedList<Item<AccelerationData>> itemList = new LinkedList<Item<AccelerationData>>();
    for (int i = 0; i < multDimArray.length; i++) {
      itemList.add(
          new Item<AccelerationData>(
              0,
              new AccelerationData(multDimArray[i][0], multDimArray[i][1], multDimArray[i][2])
          )
      );
    }
    return new TimeSeries<AccelerationData>(itemList, new LinkedList<Item<String>>());
  }
}
