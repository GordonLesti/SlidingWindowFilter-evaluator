import static org.junit.Assert.assertEquals;

import org.junit.Test;
import swf.measure.timeseries.dynamictimewarping.SakoeChibaBand;

public class SakoeChibaBandTest {
  @Test
  public void testIsConditionFulfilledEvenWidth() {
    SakoeChibaBand scb = new SakoeChibaBand(0.3);
    String[] conditionMatrix = this.createConditionMatrix10x8window3();
    for (int i = 0; i < conditionMatrix.length; i++) {
      for (int j = 0; j < conditionMatrix[i].length(); j++) {
        assertEquals(
            stringToBoolean(conditionMatrix[i].substring(j, j + 1)),
            scb.isConditionFulfilled(i, j, 10, 8)
        );
      }
    }
  }

  @Test
  public void testIsConditionFulfilledOddWidth() {
    SakoeChibaBand scb = new SakoeChibaBand(0.2);
    String[] conditionMatrix = this.createConditionMatrix4x7window3();
    for (int i = 0; i < conditionMatrix.length; i++) {
      for (int j = 0; j < conditionMatrix[i].length(); j++) {
        assertEquals(
            stringToBoolean(conditionMatrix[i].substring(j, j + 1)),
            scb.isConditionFulfilled(i, j, 4, 7)
        );
      }
    }
  }

  private boolean stringToBoolean(String str) {
    if (str.equals("0")) {
      return false;
    }
    return true;
  }

  private String[] createConditionMatrix10x8window3() {
    String[] conditionMatrix = {
        "11000000",
        "11100000",
        "11110000",
        "01111000",
        "00111100",
        "00111100",
        "00011110",
        "00001111",
        "00000111",
        "00000011"
    };
    return conditionMatrix;
  }

  private String[] createConditionMatrix4x7window3() {
    String[] conditionMatrix = {
        "1111000",
        "1111110",
        "0111111",
        "0001111"
    };
    return conditionMatrix;
  }
}
