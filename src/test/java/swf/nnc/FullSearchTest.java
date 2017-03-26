import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import org.junit.Test;
import swf.calculator.Distance;
import swf.nnc.FullSearch;
import swf.nnc.Result;

public class FullSearchTest {
  @Test
  public void searchNearestNeighbour() {
    LinkedList<Integer> list = new LinkedList<Integer>();
    list.add(-97);
    list.add(-86);
    list.add(-72);
    list.add(14);
    list.add(31);
    Distance<Integer, Integer> distanceCalculator = new Distance<Integer, Integer>() {
        public Integer calculateDistance(Integer int1, Integer int2) {
          return Math.abs(int1 - int2);
        }
    };
    FullSearch<Integer, Integer> fullSearch = new FullSearch<Integer, Integer>(distanceCalculator);
    assertEquals(
        new Result<Integer, Integer>(-72, 41),
        fullSearch.searchNearestNeighbour(-31, list)
    );
  }
}
