package swf.evaluation;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import swf.Accel;
import swf.TimeSeries;
import swf.evaluation.slidingwindow.Threshold;
import swf.evaluation.slidingwindow.WindowSize;
import swf.filter.Filter;
import swf.measure.Distance;
import swf.nnc.Factory;
import swf.nnc.NearestNeighbourClassificator;
import swf.timeseries.Point;

public class SlidingWindow implements Callable<SlidingWindow> {
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
  private int[][] truePositive;
  private int[][] trueNegative;
  private int[][] falsePositive;
  private int[][] falseNegative;
  private int[] nncCallCount;

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
    int recordCount = tsList.size();
    this.truePositive = new int[recordCount][8];
    this.trueNegative = new int[recordCount][8];
    this.falsePositive = new int[recordCount][8];
    this.falseNegative = new int[recordCount][8];
    this.nncCallCount = new int[recordCount];
  }

  /**
   * Start simulation.
   */
  public SlidingWindow call() {
    int index = 0;
    for (TimeSeries<Accel> ts : this.tsList) {
      this.slideOverTimeSeries(ts, index);
      index++;
    }
    return this;
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

  public int getNncCallCount(int recordFromIndex, int recordToIndex) {
    int sum = 0;
    for (int i = recordFromIndex; i < recordToIndex; i++) {
      sum += this.nncCallCount[i];
    }
    return sum;
  }

  public int getTruePositve(int recordIndex, int gestureIndex) {
    return this.truePositive[recordIndex][gestureIndex];
  }

  public int getTrueNegative(int recordIndex, int gestureIndex) {
    return this.trueNegative[recordIndex][gestureIndex];
  }

  public int getFalsePositive(int recordIndex, int gestureIndex) {
    return this.falsePositive[recordIndex][gestureIndex];
  }

  public int getFalseNegative(int recordIndex, int gestureIndex) {
    return this.falseNegative[recordIndex][gestureIndex];
  }

  /**
   * Returns the precision.
   */
  public double getMicroPrecision(int recordFromIndex, int recordToIndex) {
    int sumDividend = 0;
    int sumDivisor = 0;
    for (int i = recordFromIndex; i < recordToIndex; i++) {
      for (int j = 0; j < 8; j++) {
        sumDividend += this.getTruePositve(i, j);
        sumDivisor += this.getTruePositve(i, j) + this.getFalsePositive(i, j);
      }
    }
    return (1.0 * sumDividend) / sumDivisor;
  }

  /**
   * Returns the recall.
   */
  public double getMicroRecall(int recordFromIndex, int recordToIndex) {
    int sumDividend = 0;
    int sumDivisor = 0;
    for (int i = recordFromIndex; i < recordToIndex; i++) {
      for (int j = 0; j < 8; j++) {
        sumDividend += this.getTruePositve(i, j);
        sumDivisor += this.getTruePositve(i, j) + this.getFalseNegative(i, j);
      }
    }
    return (1.0 * sumDividend) / sumDivisor;
  }

  /**
   * Returns the F Score of the SlidingWindow.
   */
  public double getMicroFscore(double beta, int recordFromIndex, int recordToIndex) {
    double precision = this.getMicroPrecision(recordFromIndex, recordToIndex);
    double recall = this.getMicroRecall(recordFromIndex, recordToIndex);
    double betaSquared = beta * beta;
    return ((1 + betaSquared) * precision * recall) / (betaSquared * precision + recall);
  }

  private void slideOverTimeSeries(TimeSeries<Accel> ts, int recordIndex) {
    this.nncCallCount[recordIndex] = 0;
    LinkedList<TimeSeries<Accel>> trainingData = new LinkedList<TimeSeries<Accel>>();
    LinkedList<TimeSeries<Accel>> testData = new LinkedList<TimeSeries<Accel>>();
    for (int i = 1; i < 9; i++) {
      trainingData.add(ts.intervalByTag(Integer.toString(i)));
      testData.add(ts.intervalByTag(Integer.toString(i + 8)));
    }
    int fromIndex = ts.lastIndexOf(ts.intervalByTag(Integer.toString(8)).getLast()) + 1;
    int toIndex = ts.size();
    TimeSeries<Accel> taggedRecord = ts.subTimeSeries(fromIndex, toIndex);
    TimeSeries<Accel> record = new TimeSeries<Accel>();
    for (Point<Accel> point : taggedRecord) {
      record.add(new Point<Accel>(point.getData()));
    }
    int windowSize = this.windowSize.windowSize(trainingData);
    int stepSize = (int) (windowSize / 10.0);
    int time = 0;
    NearestNeighbourClassificator<TimeSeries<Accel>> nnc =
        this.nncFactory.create(this.distance, trainingData);
    Filter<TimeSeries<Accel>> filter = this.filterFactory.create(trainingData);
    double[] thresholds = this.threshold.threshold(trainingData, testData, this.distance);
    while (time + windowSize <= record.size()) {
      TimeSeries<Accel> window = record.subTimeSeries(time, time + windowSize);
      if (filter.filter(window)) {
        TimeSeries<Accel> nn = nnc.nearestNeighbour(window);
        this.nncCallCount[recordIndex]++;
        double dist = this.distance.distance(window, nn);
        if (dist <= thresholds[trainingData.indexOf(nn)]) {
          for (int i = time; i < time + windowSize; i++) {
            record.set(
                i,
                new Point<Accel>(
                    record.get(i).getData(),
                    Integer.toString(trainingData.indexOf(nn) + 9)
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
    int[][] matches = this.setSuccessAndFailCount(taggedRecord, record);
    this.truePositive[recordIndex] = matches[0];
    this.trueNegative[recordIndex] = matches[1];
    this.falsePositive[recordIndex] = matches[2];
    this.falseNegative[recordIndex] = matches[3];
  }

  private int[][] setSuccessAndFailCount(
      TimeSeries<Accel> taggedRecord,
      TimeSeries<Accel> record
  ) {
    int[] truePositives = new int[8];
    int[] trueNegatives = new int[8];
    int[] falsePositives = new int[8];
    int[] falseNegatives = new int[8];
    for (int i = 0; i < 8; i++) {
      String gestureIndex = Integer.toString(i + 9);
      truePositives[i] = 0;
      trueNegatives[i] = 0;
      falsePositives[i] = 0;
      falseNegatives[i] = 0;
      for (int j = 0; j < taggedRecord.size(); j++) {
        String expectedTag = taggedRecord.get(j).getTag();
        String tag = record.get(j).getTag();
        boolean isTrue = expectedTag.equals(gestureIndex) && expectedTag.equals(tag)
            || !expectedTag.equals(gestureIndex) && !tag.equals(gestureIndex);
        boolean isPositive = tag.equals(gestureIndex);
        if (isTrue) {
          if (isPositive) {
            truePositives[i]++;
          } else {
            trueNegatives[i]++;
          }
        } else {
          if (isPositive) {
            falsePositives[i]++;
          } else {
            falseNegatives[i]++;
          }
        }
      }
    }
    int[][] result = new int[4][8];
    result[0] = truePositives;
    result[1] = trueNegatives;
    result[2] = falsePositives;
    result[3] = falseNegatives;
    return result;
  }
}
