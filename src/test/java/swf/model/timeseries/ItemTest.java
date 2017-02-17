import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import swf.model.timeseries.Item;

public class ItemTest {
  @Test
  public void testGetTime() {
    Item<String> item = new Item<String>(5, "");
    assertEquals(5, item.getTime());
  }

  @Test
  public void testGetData() {
    Item<String> item = new Item<String>(0, "One");
    assertEquals("One", item.getData());
  }

  @Test
  public void testEquals() {
    Item<String> item = new Item<String>(0, "1");
    assertFalse(item.equals(new Item<String>(0, "2")));
    assertFalse(item.equals(new Item<String>(1, "1")));
    assertFalse(item.equals(new Item<String>(1, "2")));
    assertFalse(item.equals("1"));
    assertTrue(item.equals(new Item<String>(0, "1")));
  }

  @Test
  public void testHashCode() {
    int hashCode = new Item<String>(0, "1").hashCode();
    assertNotEquals(new Item<String>(0, "2").hashCode(), hashCode);
    assertNotEquals(new Item<String>(1, "1").hashCode(), hashCode);
    assertEquals(new Item<String>(0, "1").hashCode(), hashCode);
  }
}
