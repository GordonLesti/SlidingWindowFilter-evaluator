package swf.evaluation;

import java.lang.Comparable;
import java.util.LinkedList;
import java.util.List;
import swf.Accel;
import swf.TimeSeries;
import swf.evaluation.slidingwindow.Threshold;
import swf.evaluation.slidingwindow.WindowSize;
import swf.measure.Distance;
import swf.nnc.Factory;
import swf.nnc.NearestNeighbourClassificator;
import swf.timeseries.Point;

public class SlidingWindow implements Comparable<SlidingWindow> {
  private List<TimeSeries<Accel>> tsList;
  private Distance<TimeSeries<Accel>> distance;
  private Factory<TimeSeries<Accel>> nncFactory;
  private WindowSize windowSize;
  private Threshold threshold;
  private String name;
  private List<TimeSeries<Accel>> resultTsList;
  private int successCount;
  private int failCount;

  /**
   * Creates an evaluator for sliding window filters on TimeSeries of Accel data.
   */
  public SlidingWindow(
      List<TimeSeries<Accel>> tsList,
      Distance<TimeSeries<Accel>> distance,
      Factory<TimeSeries<Accel>> nncFactory,
      WindowSize windowSize,
      Threshold threshold,
      String name
  ) {
    this.tsList = tsList;
    this.distance = distance;
    this.nncFactory = nncFactory;
    this.windowSize = windowSize;
    this.threshold = threshold;
    this.name = name;
    this.successCount = 0;
    this.failCount = 0;
    for (TimeSeries<Accel> ts : tsList) {
      this.slideOverTimeSeries(ts);
    }
  }

  public String getName() {
    return this.name;
  }

  public int getSuccessCount() {
    return this.successCount;
  }

  public int getFailCount() {
    return this.failCount;
  }

  public double getSuccessQuotient() {
    return (this.getSuccessCount() * 1.0) / this.getFailCount();
  }

  public int compareTo(SlidingWindow swf) {
    double quotient = this.getSuccessQuotient();
    double swfQuotient = swf.getSuccessQuotient();
    if (quotient > swfQuotient) {
      return 1;
    }
    if (quotient < swfQuotient) {
      return -1;
    }
    return 0;
  }

  private void slideOverTimeSeries(TimeSeries<Accel> ts) {
    LinkedList<TimeSeries<Accel>> testData = new LinkedList<TimeSeries<Accel>>();
    for (int i = 1; i < 9; i++) {
      testData.add(ts.intervalByTag(Integer.toString(i)));
    }
    int fromIndex = ts.lastIndexOf(ts.intervalByTag(Integer.toString(8)).getLast()) + 1;
    int toIndex = ts.size();
    TimeSeries<Accel> taggedRecord = ts.subTimeSeries(fromIndex, toIndex);
    TimeSeries<Accel> record = new TimeSeries<Accel>();
    for (Point<Accel> point : taggedRecord) {
      record.add(new Point<Accel>(point.getData()));
    }
    int windowSize = this.windowSize.windowSize(testData);
    int stepSize = (int) (windowSize / 10.0);
    int time = 0;
    NearestNeighbourClassificator<TimeSeries<Accel>> nnc =
        this.nncFactory.create(this.distance, testData);
    double threshold = this.threshold.threshold(testData, this.distance);
    while (time + windowSize <= record.size()) {
      TimeSeries<Accel> window = record.subTimeSeries(time, time + windowSize);
      TimeSeries<Accel> nn = nnc.nearestNeighbour(window);
      double dist = this.distance.distance(window, nn);
      if (dist < threshold) {
        for (int i = time; i < time + windowSize; i++) {
          record.set(
              i,
              new Point<Accel>(
                  record.get(i).getData(),
                  Integer.toString(testData.indexOf(nn) + 9)
              )
          );
        }
        time += windowSize;
      } else {
        time += stepSize;
      }
    }
    this.setSuccessAndFailCount(taggedRecord, record);
  }

  private void setSuccessAndFailCount(
      TimeSeries<Accel> taggedRecord,
      TimeSeries<Accel> record
  ) {
    for (int i = 0; i < taggedRecord.size(); i++) {
      String expectedTag = taggedRecord.get(i).getTag();
      String tag = record.get(i).getTag();
      if (!expectedTag.equals(tag)) {
        this.failCount++;
      } else if (!expectedTag.equals("")) {
        this.successCount++;
      }
    }
  }
}
