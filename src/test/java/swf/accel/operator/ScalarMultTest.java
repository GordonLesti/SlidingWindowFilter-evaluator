import static org.junit.Assert.assertEquals;

import org.junit.Test;
import swf.Accel;
import swf.accel.operator.ScalarMult;

public class ScalarMultTest {
  @Test
  public void testMult() {
    assertEquals(
        new Accel(276, 804, 260),
        new ScalarMult().mult(
            -8.12,
            new Accel(-34, -99, -32)
        )
    );
  }
}
