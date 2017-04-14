import static org.junit.Assert.assertEquals;

import org.junit.Test;
import swf.Accel;
import swf.accel.operator.Add;

public class AddTest {
  @Test
  public void testAdd() {
    assertEquals(
        new Accel(-130, -126, -36),
        new Add().add(
            new Accel(-45, -70, -77),
            new Accel(-85, -56, 41)
        )
    );
  }
}
