package swf.evaluation;

import java.lang.Comparable;
import java.util.LinkedList;
import java.util.List;
import swf.Accel;
import swf.TimeSeries;
import swf.nnc.Factory;
import swf.nnc.NearestNeighbourClassificator;

public class Distance implements Comparable<Distance> {
  private List<TimeSeries<Accel>> tsList;
  private swf.measure.Distance<TimeSeries<Accel>> distance;
  private Factory<TimeSeries<Accel>> nncFactory;
  private String name;
  private double successQuotient;

  /**
   * Creates an evaluator for distance measure functions on TimeSeries of type Accel.
   */
  public Distance(
      List<TimeSeries<Accel>> tsList,
      swf.measure.Distance<TimeSeries<Accel>> distance,
      Factory<TimeSeries<Accel>> nncFactory,
      String name
  ) {
    this.tsList = tsList;
    this.distance = distance;
    this.nncFactory = nncFactory;
    this.name = name;
    int successCount = 0;
    int failCount = 0;
    for (TimeSeries<Accel> ts : this.tsList) {
      LinkedList<TimeSeries<Accel>> testData = new LinkedList<TimeSeries<Accel>>();
      for (int i = 1; i < 9; i++) {
        testData.add(ts.intervalByTag(Integer.toString(i)));
      }
      NearestNeighbourClassificator<TimeSeries<Accel>> nnc =
          this.nncFactory.create(this.distance, testData);
      for (int i = 9; i < 17; i++) {
        TimeSeries<Accel> nn = nnc.nearestNeighbour(ts.intervalByTag(Integer.toString(i)));
        if (testData.indexOf(nn) == i - 9) {
          successCount++;
        } else {
          failCount++;
        }
      }
    }
    this.successQuotient = (successCount * 1.0) / (successCount + failCount);
  }

  public String getName() {
    return this.name;
  }

  public double getSuccessQuotient() {
    return this.successQuotient;
  }

  /**
   * Compares two Distance evaluator by the success quotient.
   */
  public int compareTo(Distance dist) {
    if (this.getSuccessQuotient() < dist.getSuccessQuotient()) {
      return -1;
    }
    if (this.getSuccessQuotient() > dist.getSuccessQuotient()) {
      return 1;
    }
    return 0;
  }
}
