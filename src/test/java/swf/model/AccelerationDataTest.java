import static org.junit.Assert.assertEquals;

import org.junit.Test;
import swf.model.AccelerationData;

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
}
