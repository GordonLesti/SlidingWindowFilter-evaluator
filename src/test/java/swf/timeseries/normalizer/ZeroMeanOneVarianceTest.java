import static org.junit.Assert.assertEquals;

import org.junit.Test;
import swf.TimeSeries;
import swf.measure.Distance;
import swf.operator.Add;
import swf.operator.ScalarMult;
import swf.timeseries.Point;
import swf.timeseries.normalizer.ZeroMeanOneVariance;

public class ZeroMeanOneVarianceTest {
  @Test
  public void testNormalizer() {
    double[] data = {66.85, 59.81, 18.86, -83.07, -83.12};
    TimeSeries<Double> ts = new TimeSeries<Double>();
    double deviation = Math.sqrt(22125.96772 / 4);
    double[] normData = {
        70.984 / deviation,
        63.944 / deviation,
        22.994 / deviation,
        (-83.07 + 4.134) / deviation,
        -78.986 / deviation
    };
    TimeSeries<Double> normTs = new TimeSeries<Double>();
    for (int i = 0; i < data.length; i++) {
      ts.add(new Point<Double>(data[i], "eZeWQ9HL"));
      normTs.add(new Point<Double>(normData[i], "eZeWQ9HL"));
    }
    assertEquals(
        normTs,
        new ZeroMeanOneVariance<Double>(
            new Distance<Double>() {
              public double distance(Double num1, Double num2) {
                return Math.abs(num1 - num2);
              }
            },
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
