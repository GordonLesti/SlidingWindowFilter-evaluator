import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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

  @Test
  public void testEquals() {
    LinkedList<Item<String>> items1 = new LinkedList<Item<String>>();
    LinkedList<Item<String>> flags1 = new LinkedList<Item<String>>();
    LinkedList<Item<String>> items2 = new LinkedList<Item<String>>();
    LinkedList<Item<String>> flags2 = new LinkedList<Item<String>>();
    TimeSeries<String> timeSeries1 = new TimeSeries<String>(items1, flags1);
    TimeSeries<String> timeSeries2 = new TimeSeries<String>(items2, flags2);
    assertFalse(timeSeries1.equals(""));
    assertTrue(timeSeries1.equals(timeSeries2));
    items1.add(new Item<String>(0, "1"));
    assertFalse(timeSeries1.equals(timeSeries2));
    items2.add(new Item<String>(0, "1"));
    assertTrue(timeSeries1.equals(timeSeries2));
    flags1.add(new Item<String>(0, "1"));
    assertFalse(timeSeries1.equals(timeSeries2));
    flags2.add(new Item<String>(0, "1"));
    assertTrue(timeSeries1.equals(timeSeries2));
  }

  @Test
  public void testHashCode() {
    LinkedList<Item<String>> items1 = new LinkedList<Item<String>>();
    LinkedList<Item<String>> flags1 = new LinkedList<Item<String>>();
    LinkedList<Item<String>> items2 = new LinkedList<Item<String>>();
    LinkedList<Item<String>> flags2 = new LinkedList<Item<String>>();
    TimeSeries<String> timeSeries1 = new TimeSeries<String>(items1, flags1);
    TimeSeries<String> timeSeries2 = new TimeSeries<String>(items2, flags2);
    int hashCode1 = timeSeries1.hashCode();
    assertEquals(timeSeries1.hashCode(), timeSeries2.hashCode());
    items1.add(new Item<String>(0, "1"));
    assertNotEquals(timeSeries1.hashCode(), timeSeries2.hashCode());
    items2.add(new Item<String>(0, "1"));
    assertEquals(timeSeries1.hashCode(), timeSeries2.hashCode());
    flags1.add(new Item<String>(0, "1"));
    assertNotEquals(timeSeries1.hashCode(), timeSeries2.hashCode());
    flags2.add(new Item<String>(0, "1"));
    assertEquals(timeSeries1.hashCode(), timeSeries2.hashCode());
  }
}
