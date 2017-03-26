import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import swf.nnc.Result;

public class ResultTest {
  @Test
  public void testGetObject() {
    Result<String, Integer> result = new Result<String, Integer>("uiurZCma", 66);
    assertEquals("uiurZCma", result.getObject());
  }

  @Test
  public void testGetDistance() {
    Result<String, Integer> result = new Result<String, Integer>("T51LgAOT", -51);
    assertEquals(-51, (int) result.getDistance());
  }

  @Test
  public void testEquals() {
    Result<String, Integer> result = new Result<String, Integer>("wxCTTnNP", -25);
    assertFalse(result.equals(new Result<String, Integer>("aHmDvReN", -25)));
    assertFalse(result.equals(new Result<String, Integer>("wxCTTnNP", -33)));
    assertFalse(result.equals(new Result<String, Integer>("SJzWKtbZ", -22)));
    assertFalse(result.equals("GylEwcOS"));
    assertTrue(result.equals(new Result<String, Integer>("wxCTTnNP", -25)));
  }

  @Test
  public void testHashCode() {
    int hashCode = new Result<String, Integer>("FWzPYFjH", -75).hashCode();
    assertNotEquals(new Result<String, Integer>("b1ew9Z9A", -75).hashCode(), hashCode);
    assertNotEquals(new Result<String, Integer>("FWzPYFjH", 26).hashCode(), hashCode);
    assertEquals(new Result<String, Integer>("FWzPYFjH", -75).hashCode(), hashCode);
  }
}
