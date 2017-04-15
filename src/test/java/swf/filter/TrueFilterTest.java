import static org.junit.Assert.assertTrue;

import org.junit.Test;
import swf.filter.TrueFilter;

public class TrueFilterTest {
  @Test
  public void testFilter() {
    assertTrue(new TrueFilter<String>().filter("NrkQvTWy"));
  }
}
