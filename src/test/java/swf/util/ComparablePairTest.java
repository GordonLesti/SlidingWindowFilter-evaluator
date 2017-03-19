import static org.junit.Assert.assertEquals;

import org.junit.Test;
import swf.util.ComparablePair;

public class ComparablePairTest {
  @Test
  public void testGetContent() {
    ComparablePair<String, Double> comPair =
        new ComparablePair<String, Double>("YCDEdlz1SGjbM4AU", 5.8);
    assertEquals("YCDEdlz1SGjbM4AU", comPair.getContent());
  }

  @Test
  public void testCompareTo() {
    ComparablePair<String, Double> comPair1 =
        new ComparablePair<String, Double>("Kj89zWI4VHySEvnM", 874.62);
    ComparablePair<String, Double> comPair2 =
        new ComparablePair<String, Double>("XkTRGN81zlz4pCIN", 321.64);
    ComparablePair<String, Double> comPair3 =
        new ComparablePair<String, Double>("dmsYZvxq9aV8KEmM", 321.64);
    assertEquals(1, comPair1.compareTo(comPair2));
    assertEquals(-1, comPair3.compareTo(comPair1));
    assertEquals(0, comPair2.compareTo(comPair3));
  }
}
