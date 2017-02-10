import static org.junit.Assert.assertEquals;

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
}
