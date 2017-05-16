import static org.junit.Assert.assertEquals;

import org.junit.Test;
import swf.TimeSeries;
import swf.measure.Distance;
import swf.measure.timeseries.NormalizedDistance;
import swf.operator.Add;
import swf.operator.ScalarMult;
import swf.timeseries.Point;

public class NormalizedDistanceTest {
  @Test
  public void testDistance() {
    TimeSeries<Integer> ts1 = new TimeSeries<Integer>();
    ts1.add(new Point<Integer>(-7));
    ts1.add(new Point<Integer>(9));
    ts1.add(new Point<Integer>(4));
    TimeSeries<Integer> ts2 = new TimeSeries<Integer>();
    ts2.add(new Point<Integer>(-3));
    ts2.add(new Point<Integer>(-7));
    ts2.add(new Point<Integer>(4));
    ts2.add(new Point<Integer>(-5));
    assertEquals(
        65,
        new NormalizedDistance<Integer>(
            new Distance<TimeSeries<Integer>>() {
              public double distance(TimeSeries<Integer> intTs1, TimeSeries<Integer> intTs2) {
                int sum1 = 0;
                for (Point<Integer> point : intTs1) {
                  Integer data = point.getData();
                  sum1 += data * data;
                }
                int sum2 = 0;
                for (Point<Integer> point : intTs2) {
                  Integer data = point.getData();
                  sum2 += data * data;
                }
                return Math.abs(sum1 - sum2);
              }
            },
            new Add<Integer>() {
              public Integer add(Integer int1, Integer int2) {
                return int1 + int2;
              }
            },
            new ScalarMult<Integer>() {
              public Integer mult(double scalar, Integer num) {
                return (int) Math.round(scalar * num);
              }
            }
        ).distance(ts1, ts2),
        0
    );
  }
}
