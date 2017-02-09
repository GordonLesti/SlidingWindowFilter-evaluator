import static org.junit.Assert.assertSame;

import java.util.LinkedList;
import org.junit.Test;
import swf.model.TimeSeries;
import swf.model.timeseries.Item;

public class TimeSeriesTest {

  @Test
  public void testGetItems() {
    LinkedList<Item<String>> list = new LinkedList<Item<String>>();
    list.add(new Item<String>(0, "First"));
    list.add(new Item<String>(1, "Second"));
    TimeSeries timeSeries = new TimeSeries<String>(list);
    assertSame(list, timeSeries.getItems());
  }
}
