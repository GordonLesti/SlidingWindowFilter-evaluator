import static org.junit.Assert.assertEquals;

import org.junit.Test;
import swf.measure.timeseries.dynamictimewarping.SakoeChibaBand;

public class SakoeChibaBandTest {
  @Test
  public void testIsConditionFulfilledEvenWidth() {
    SakoeChibaBand scb = new SakoeChibaBand(0.3);
    boolean[][] conditionMatrix = this.createConditionMatrix10x8window3();
    for (int i = 0; i < conditionMatrix.length; i++) {
      for (int j = 0; j < conditionMatrix[i].length; j++) {
        assertEquals(conditionMatrix[i][j], scb.isConditionFulfilled(i, j, 10, 8));
      }
    }
  }

  @Test
  public void testIsConditionFulfilledOddWidth() {
    SakoeChibaBand scb = new SakoeChibaBand(0.2);
    boolean[][] conditionMatrix = this.createConditionMatrix4x7window3();
    for (int i = 0; i < conditionMatrix.length; i++) {
      for (int j = 0; j < conditionMatrix[i].length; j++) {
        assertEquals(conditionMatrix[i][j], scb.isConditionFulfilled(i, j, 4, 7));
      }
    }
  }

  private boolean[][] createConditionMatrix10x8window3() {
    boolean[][] conditionMatrix = {
        {true, true, false, false, false, false, false, false},
        {true, true, true, false, false, false, false, false},
        {true, true, true, true, false, false, false, false},
        {false, true, true, true, true, false, false, false},
        {false, false, true, true, true, true, false, false},
        {false, false, true, true, true, true, false, false},
        {false, false, false, true, true, true, true, false},
        {false, false, false, false, true, true, true, true},
        {false, false, false, false, false, true, true, true},
        {false, false, false, false, false, false, true, true}
    };
    return conditionMatrix;
  }

  private boolean[][] createConditionMatrix4x7window3() {
    boolean[][] conditionMatrix = {
        {true, true, true, true, false, false, false},
        {true, true, true, true, true, true, false},
        {false, true, true, true, true, true, true},
        {false, false, false, true, true, true, true}
    };
    return conditionMatrix;
  }
}
