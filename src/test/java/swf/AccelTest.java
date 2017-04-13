import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import swf.Accel;

public class AccelTest {
  @Test
  public void testGetX() {
    assertEquals(-891, new Accel(-891, 334, 586).getX());
  }

  @Test
  public void testGetY() {
    assertEquals(-806, new Accel(158, -806, -119).getY());
  }

  @Test
  public void testGetZ() {
    assertEquals(-705, new Accel(-543, 993, -705).getZ());
  }

  @Test
  public void testEquals() {
    Accel accel = new Accel(-204, 542, 129);
    assertFalse(accel.equals(new Accel(111, 542, 129)));
    assertFalse(accel.equals(new Accel(-204, 662, 129)));
    assertFalse(accel.equals(new Accel(-204, 542, -243)));
    assertFalse(accel.equals("a1zuGxZ2"));
    assertTrue(accel.equals(new Accel(-204, 542, 129)));
  }

  @Test
  public void testHashCode() {
    int hashCode = new Accel(120, -100, -527).hashCode();
    assertNotEquals(hashCode, new Accel(580, -100, -527).hashCode());
    assertNotEquals(hashCode, new Accel(120, -383, -527).hashCode());
    assertNotEquals(hashCode, new Accel(120, -100, -311).hashCode());
    assertEquals(hashCode, new Accel(120, -100, -527).hashCode());
  }

  @Test
  public void testToString() {
    assertEquals("(631,426,-296)", new Accel(631, 426, -296).toString());
  }
}
