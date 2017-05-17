import static org.junit.Assert.assertEquals;

import org.junit.Test;
import swf.TimeSeries;
import swf.operator.Add;
import swf.operator.ScalarMult;
import swf.timeseries.Point;
import swf.timeseries.normalizer.ZeroMean;

public class ZeroMeanTest {
  @Test
  public void testNormalizer() {
    double[] data = {-93.67, -7.3, -85.91, 90.47, 1.02};
    TimeSeries<Double> ts = new TimeSeries<Double>();
    double[] normData = {-74.592, -7.3 + 19.078, -66.832, 109.548, 20.098};
    TimeSeries<Double> normTs = new TimeSeries<Double>();
    for (int i = 0; i < data.length; i++) {
      ts.add(new Point<Double>(data[i], "2s5MqyDL"));
      normTs.add(new Point<Double>(normData[i], "2s5MqyDL"));
    }
    assertEquals(
        normTs,
        new ZeroMean<Double>(
            new Add<Double>() {
              public Double add(Double num1, Double num2) {
                return num1 + num2;
              }
            },
            new ScalarMult<Double>() {
              public Double mult(double scalar, Double num) {
                return scalar * num;
              }
            }
        ).normalize(ts)
    );
  }
}
