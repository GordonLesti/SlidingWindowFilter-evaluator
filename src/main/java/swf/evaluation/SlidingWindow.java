package swf.evaluation;

import java.lang.Comparable;
import java.util.LinkedList;
import java.util.List;
import swf.Accel;
import swf.TimeSeries;
import swf.evaluation.slidingwindow.Threshold;
import swf.evaluation.slidingwindow.WindowSize;
import swf.filter.Filter;
import swf.measure.Distance;
import swf.nnc.Factory;
import swf.nnc.NearestNeighbourClassificator;
import swf.timeseries.Point;

public class SlidingWindow implements Comparable<SlidingWindow> {
  private List<TimeSeries<Accel>> tsList;
  private Distance<TimeSeries<Accel>> distance;
  private Factory<TimeSeries<Accel>> nncFactory;
  private swf.filter.Factory<TimeSeries<Accel>> filterFactory;
  private WindowSize windowSize;
  private Threshold threshold;
  private String distName;
  private String filterName;
  private String windowSizeName;
  private String thresholdName;
  private List<TimeSeries<Accel>> resultTsList;
  private int[] truePositive;
  private int[] trueNegative;
  private int[] falsePositive;
  private int[] falseNegative;
  private int nncCallCount;

  /**
   * Creates an evaluator for sliding window filters on TimeSeries of Accel data.
   */
  public SlidingWindow(
      List<TimeSeries<Accel>> tsList,
      Distance<TimeSeries<Accel>> distance,
      Factory<TimeSeries<Accel>> nncFactory,
      swf.filter.Factory<TimeSeries<Accel>> filterFactory,
      WindowSize windowSize,
      Threshold threshold,
      String distName,
      String filterName,
      String windowSizeName,
      String thresholdName
  ) {
    this.tsList = tsList;
    this.distance = distance;
    this.nncFactory = nncFactory;
    this.filterFactory = filterFactory;
    this.windowSize = windowSize;
    this.threshold = threshold;
    this.distName = distName;
    this.filterName = filterName;
    this.windowSizeName = windowSizeName;
    this.thresholdName = thresholdName;
    this.truePositive = new int[8];
    this.trueNegative = new int[8];
    this.falsePositive = new int[8];
    this.falseNegative = new int[8];
    for (int i = 0; i < 8; i++) {
      this.truePositive[i] = 0;
      this.trueNegative[i] = 0;
      this.falsePositive[i] = 0;
      this.falseNegative[i] = 0;
    }
    this.nncCallCount = 0;
    for (TimeSeries<Accel> ts : tsList) {
      this.slideOverTimeSeries(ts);
    }
  }

  public String getDistName() {
    return this.distName;
  }

  public String getFilterName() {
    return this.filterName;
  }

  public String getWindowSizeName() {
    return this.windowSizeName;
  }

  public String getThesholdName() {
    return this.thresholdName;
  }

  public int getNncCallCount() {
    return this.nncCallCount;
  }

  public int getTruePositve(int index) {
    return this.truePositive[index];
  }

  public int getTrueNegative(int index) {
    return this.trueNegative[index];
  }

  public int getFalsePositive(int index) {
    return this.falsePositive[index];
  }

  public int getFalseNegative(int index) {
    return this.falseNegative[index];
  }

  /**
   * Returns the precision.
   */
  public double getMicroPrecision() {
    int sumDividend = 0;
    int sumDivisor = 0;
    for (int i = 0; i < 8; i++) {
      sumDividend += this.getTruePositve(i);
      sumDivisor += this.getTruePositve(i) + this.getFalsePositive(i);
    }
    return (1.0 * sumDividend) / sumDivisor;
  }

  /**
   * Returns the recall.
   */
  public double getMicroRecall() {
    int sumDividend = 0;
    int sumDivisor = 0;
    for (int i = 0; i < 8; i++) {
      sumDividend += this.getTruePositve(i);
      sumDivisor += this.getTruePositve(i) + this.getFalseNegative(i);
    }
    return (1.0 * sumDividend) / sumDivisor;
  }

  /**
   * Returns the average accuracy.
   */
  public double getAverageAccuracy() {
    double sum = 0;
    for (int i = 0; i < 8; i++) {
      sum += (1.0 * (this.getTruePositve(i) + this.getTrueNegative(i)))
          / (this.getTruePositve(i) + this.getFalseNegative(i)
          + this.getFalsePositive(i) + this.getTrueNegative(i));
    }
    return sum / 8;
  }

  /**
   * Returns the F Score of the SlidingWindow.
   */
  public double getFscore(double beta) {
    double precision = this.getMicroPrecision();
    double recall = this.getMicroRecall();
    double betaSquared = beta * beta;
    return ((1 + betaSquared) * precision * recall) / (betaSquared * precision + recall);
  }

  /**
   * Compares two SlidingWindow by f1Score.
   */
  public int compareTo(SlidingWindow swf) {
    double f1Score = this.getFscore(1);
    double swfF1Score = swf.getFscore(1);
    if (Double.isNaN(f1Score) && Double.isNaN(swfF1Score)) {
      return 0;
    }
    if (f1Score > swfF1Score || Double.isNaN(swfF1Score)) {
      return 1;
    }
    if (f1Score < swfF1Score || Double.isNaN(f1Score)) {
      return -1;
    }
    int nncCount = this.getNncCallCount();
    int swfNnCCount = swf.getNncCallCount();
    if (nncCount < swfNnCCount) {
      return 1;
    }
    if (nncCount > swfNnCCount) {
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
    Filter<TimeSeries<Accel>> filter = this.filterFactory.create(testData);
    double[] thresholds = this.threshold.threshold(testData, this.distance);
    while (time + windowSize <= record.size()) {
      TimeSeries<Accel> window = record.subTimeSeries(time, time + windowSize);
      if (filter.filter(window)) {
        TimeSeries<Accel> nn = nnc.nearestNeighbour(window);
        this.nncCallCount++;
        double dist = this.distance.distance(window, nn);
        if (dist <= thresholds[testData.indexOf(nn)]) {
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
    for (int i = 0; i < 8; i++) {
      String gestureIndex = Integer.toString(i + 9);
      for (int j = 0; j < taggedRecord.size(); j++) {
        String expectedTag = taggedRecord.get(j).getTag();
        String tag = record.get(j).getTag();
        boolean isTrue = expectedTag.equals(gestureIndex) && expectedTag.equals(tag)
            || !expectedTag.equals(gestureIndex) && !tag.equals(gestureIndex);
        boolean isPositive = tag.equals(gestureIndex);
        if (isTrue) {
          if (isPositive) {
            this.truePositive[i]++;
          } else {
            this.trueNegative[i]++;
          }
        } else {
          if (isPositive) {
            this.falsePositive[i]++;
          } else {
            this.falseNegative[i]++;
          }
        }
      }
    }
  }
}
