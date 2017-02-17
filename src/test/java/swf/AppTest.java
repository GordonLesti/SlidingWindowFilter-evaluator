import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import org.junit.Test;
import swf.App;

public class AppTest {
  @Test
  public void testGetRecordFilepaths() {
    List<String> list = App.getRecordFilenames("build/resources/test");
    assertTrue(list.contains("record1.txt"));
    assertTrue(list.contains("record2.txt"));
    assertEquals(2, list.size());
  }
}
