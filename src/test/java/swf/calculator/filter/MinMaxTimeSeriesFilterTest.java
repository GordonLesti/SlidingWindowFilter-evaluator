import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import org.junit.Test;
import swf.calculator.Measure;
import swf.calculator.filter.MinMaxTimeSeriesFilter;
import swf.model.TimeSeries;
import swf.model.timeseries.Item;

public class MinMaxTimeSeriesFilterTest {
  @Test
  public void testFilter() {
    LinkedList<TimeSeries<Integer>> library = new LinkedList<TimeSeries<Integer>>();
    library.add(this.createTimeSeriesFromInteger(83));
    library.add(this.createTimeSeriesFromInteger(29));
    Measure<Double, TimeSeries<Integer>> mean = new Measure<Double, TimeSeries<Integer>>() {
      public Double calculate(TimeSeries<Integer> timeSeries) {
        return new Double(timeSeries.getItems().get(0).getData());
      }
    };
    MinMaxTimeSeriesFilter<Integer> minMaxTimeSeriesFilter =
        new MinMaxTimeSeriesFilter<Integer>(mean, library, 1.1);
    assertFalse(minMaxTimeSeriesFilter.filter(this.createTimeSeriesFromInteger(100)));
    assertFalse(minMaxTimeSeriesFilter.filter(this.createTimeSeriesFromInteger(89)));
    assertTrue(minMaxTimeSeriesFilter.filter(this.createTimeSeriesFromInteger(88)));
    assertTrue(minMaxTimeSeriesFilter.filter(this.createTimeSeriesFromInteger(68)));
    assertTrue(minMaxTimeSeriesFilter.filter(this.createTimeSeriesFromInteger(24)));
    assertFalse(minMaxTimeSeriesFilter.filter(this.createTimeSeriesFromInteger(23)));
    assertFalse(minMaxTimeSeriesFilter.filter(this.createTimeSeriesFromInteger(10)));
  }

  private TimeSeries<Integer> createTimeSeriesFromInteger(int intValue) {
    LinkedList<Item<Integer>> items = new LinkedList<Item<Integer>>();
    items.add(new Item<Integer>(0, intValue));
    return new TimeSeries<Integer>(items, new LinkedList<Item<String>>());
  }
}
