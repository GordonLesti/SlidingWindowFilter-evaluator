import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import swf.timeseries.Point;

public class PointTest {
  @Test
  public void testGetData() {
    assertEquals("nQVChvDv", new Point<String>("nQVChvDv", "Gxl8cZre").getData());
  }

  @Test
  public void testGetTag() {
    assertEquals("f3HsUO2x", new Point<String>("eNil5zW6", "f3HsUO2x").getTag());
  }

  @Test
  public void testEquals() {
    Point<String> point = new Point<String>("RC5x83IN", "TzW7jrXK");
    assertFalse(point.equals(new Point<String>("DLGgw8FA", "TzW7jrXK")));
    assertFalse(point.equals(new Point<String>("RC5x83IN", "TiBy6guc")));
    assertFalse(point.equals("RC5x83IN"));
    assertTrue(point.equals(new Point<String>("RC5x83IN", "TzW7jrXK")));
  }

  @Test
  public void testHashCode() {
    int hashCode = new Point<String>("6HZmfI7v", "rIxNU0Cj").hashCode();
    assertNotEquals(hashCode, new Point<String>("ZnaPT5FQ", "rIxNU0Cj").hashCode());
    assertNotEquals(hashCode, new Point<String>("6HZmfI7v", "QzshmXHc").hashCode());
    assertEquals(hashCode, new Point<String>("6HZmfI7v", "rIxNU0Cj").hashCode());
  }

  @Test
  public void testToString() {
    assertEquals("Vx1I5uQq", new Point<String>("Vx1I5uQq", "").toString());
    assertEquals("XS6mMijL (I5UmzIFq)", new Point<String>("XS6mMijL", "I5UmzIFq").toString());
  }
}
