import static org.junit.Assert.assertSame;

import java.util.LinkedList;
import org.junit.Test;
import swf.model.TimeSeries;
import swf.model.timeseries.Item;

public class TimeSeriesTest {

  @Test
  public void testGetItems() {
    LinkedList<Item<String>> items = new LinkedList<Item<String>>();
    items.add(new Item<String>(0, "First"));
    items.add(new Item<String>(1, "Second"));
    TimeSeries timeSeries = new TimeSeries<String>(items, new LinkedList<Item<String>>());
    assertSame(items, timeSeries.getItems());
  }

  @Test
  public void testGetFlags() {
    LinkedList<Item<String>> flags = new LinkedList<Item<String>>();
    flags.add(new Item<String>(0, "First"));
    flags.add(new Item<String>(1, "Second"));
    TimeSeries timeSeries = new TimeSeries<String>(new LinkedList<Item<String>>(), flags);
    assertSame(flags, timeSeries.getFlags());
  }
}
