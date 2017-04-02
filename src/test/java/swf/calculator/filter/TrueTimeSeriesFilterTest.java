import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import org.junit.Test;
import swf.calculator.filter.TrueTimeSeriesFilter;
import swf.model.TimeSeries;
import swf.model.timeseries.Item;

public class TrueTimeSeriesFilterTest {
  @Test
  public void testFilter() {
    TrueTimeSeriesFilter<Integer> trueTimeSeriesFilter = new TrueTimeSeriesFilter<Integer>();
    assertTrue(
        trueTimeSeriesFilter.filter(
            new TimeSeries<Integer>(
                new LinkedList<Item<Integer>>(),
                new LinkedList<Item<String>>()
            )
        )
    );
  }
}
