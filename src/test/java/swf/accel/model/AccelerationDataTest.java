import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import swf.accel.model.AccelerationData;

public class AccelerationDataTest {
  @Test
  public void testGetX() {
    AccelerationData data = new AccelerationData(1, 2, 3);
    assertEquals(1, data.getX());
  }

  @Test
  public void testGetY() {
    AccelerationData data = new AccelerationData(1, 2, 3);
    assertEquals(2, data.getY());
  }

  @Test
  public void testGetZ() {
    AccelerationData data = new AccelerationData(1, 2, 3);
    assertEquals(3, data.getZ());
  }

  @Test
  public void testEquals() {
    AccelerationData data = new AccelerationData(1, 2, 3);
    assertFalse(data.equals(new AccelerationData(-1, 2, 3)));
    assertFalse(data.equals(new AccelerationData(1, -2, 3)));
    assertFalse(data.equals(new AccelerationData(1, 2, -3)));
    assertFalse(data.equals("123"));
    assertTrue(data.equals(new AccelerationData(1, 2, 3)));
  }

  @Test
  public void testHashCode() {
    int hashCode = new AccelerationData(1, 2, 3).hashCode();
    assertNotEquals(new AccelerationData(-1, 2, 3).hashCode(), hashCode);
    assertNotEquals(new AccelerationData(1, -2, 3).hashCode(), hashCode);
    assertNotEquals(new AccelerationData(1, 2, -3).hashCode(), hashCode);
    assertEquals(new AccelerationData(1, 2, 3).hashCode(), hashCode);
  }

  @Test
  public void testToString() {
    assertEquals("[1, 2, 3]", new AccelerationData(1, 2, 3).toString());
  }
}
