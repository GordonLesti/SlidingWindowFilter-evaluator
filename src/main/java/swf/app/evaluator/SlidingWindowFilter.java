package swf.app.evaluator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import swf.accel.model.AccelerationData;
import swf.app.Evaluator;
import swf.calculator.Filter;
import swf.calculator.Measure;
import swf.calculator.filter.MinMaxTimeSeriesFilter;
import swf.calculator.filter.TrueTimeSeriesFilter;
import swf.model.TimeSeries;
import swf.model.timeseries.Item;
import swf.nnc.NearestNeighbourClassificator;
import swf.nnc.Result;
import swf.transformer.SubTransformer;
import swf.transformer.SubWindowTransformer;

public class SlidingWindowFilter implements Evaluator {
  private Measure<Double, TimeSeries<AccelerationData>> measure;
  private NearestNeighbourClassificator<TimeSeries<AccelerationData>, Double> nnc;
  private double stepSizeFactor;
  private double distanceBlurFactor;

  /**
   * Creates a evaluator to explore a TimeSeries stream.
   */
  public SlidingWindowFilter(
      Measure<Double, TimeSeries<AccelerationData>> measure,
      NearestNeighbourClassificator<TimeSeries<AccelerationData>, Double> nnc,
      double stepSizeFactor,
      double distanceBlurFactor
  ) {
    this.measure = measure;
    this.nnc = nnc;
    this.stepSizeFactor = stepSizeFactor;
    this.distanceBlurFactor = distanceBlurFactor;
  }

  /**
   * Generates information from a list of TimeSeries.
   */
  public String evaluate(List<TimeSeries<AccelerationData>> timeSeriesList) {
    Iterator<TimeSeries<AccelerationData>> iterator = timeSeriesList.iterator();
    String output = "";
    int index = 1;
    while (iterator.hasNext()) {
      output += "Record " + index + ":\n" + this.evaluateTimeSeries(iterator.next()) + "\n";
      index++;
    }
    return output;
  }

  private String evaluateTimeSeries(TimeSeries<AccelerationData> timeSeries) {
    LinkedList<TimeSeries<AccelerationData>> library =
        new LinkedList<TimeSeries<AccelerationData>>();
    LinkedList<TimeSeries<AccelerationData>> gestures =
        new LinkedList<TimeSeries<AccelerationData>>();
    SubTransformer<AccelerationData> subTransformer;
    String output = "";
    for (int i = 1; i < 9; i++) {
      subTransformer = new SubTransformer<AccelerationData>("START " + i, "END " + i);
      library.add(subTransformer.transform(timeSeries));
      subTransformer = new SubTransformer<AccelerationData>("START " + (i + 8), "END " + (i + 8));
      gestures.add(subTransformer.transform(timeSeries));
    }
    subTransformer = new SubTransformer<AccelerationData>("END 8", "START 17");
    TimeSeries<AccelerationData> fullStreamTimeSeries = subTransformer.transform(timeSeries);
    output += this.gestureFlagsString(fullStreamTimeSeries);
    int windowSize = this.getAverageLength(library);
    double[] maxDistMap = this.getMaxNearestNeighbourDistanceMap(library, gestures);
    Filter<TimeSeries<AccelerationData>> filter = this.createFilter(library);
    int stepSize = (int) Math.ceil(this.stepSizeFactor * windowSize);
    output += "Found gestures:\n";
    output += "WindowSize: " + windowSize + "\n";
    output += "StepSize: " + stepSize + "\n";
    int foundGestureCount = 0;
    long[] minMaxTime = this.getMinMaxItemTime(fullStreamTimeSeries);
    long maxTime = minMaxTime[1];
    long currentTime = minMaxTime[0];
    while (currentTime < maxTime) {
      SubWindowTransformer<AccelerationData> subWindowTransformer =
          new SubWindowTransformer<AccelerationData>(currentTime, currentTime + windowSize);
      TimeSeries<AccelerationData> windowTimeSeries =
          subWindowTransformer.transform(fullStreamTimeSeries);
      if (filter.filter(windowTimeSeries)) {
        Result<TimeSeries<AccelerationData>, Double> result =
            this.nnc.searchNearestNeighbour(windowTimeSeries, library);
        int libIndex = library.indexOf(result.getObject());
        if (result.getDistance() < maxDistMap[libIndex]) {
          foundGestureCount++;
          long[] windowMinMax = this.getMinMaxItemTime(windowTimeSeries);
          output += "START " + (libIndex + 9) + " " + windowMinMax[0] + " END "
              + (libIndex + 9) + " " + windowMinMax[1] + "\n";
          currentTime = windowMinMax[1];
        } else {
          currentTime += stepSize;
        }
      } else {
        currentTime += stepSize;
      }
    }
    output += "Found: " + foundGestureCount + "\n";
    return output;
  }

  private String gestureFlagsString(TimeSeries<AccelerationData> timeSeries) {
    String output = "Flaged gestures:\n";
    List<Item<String>> flagList = timeSeries.getFlags();
    for (int i = 0; i < 8; i++) {
      Item<String> startItem = flagList.get(i * 2 + 1);
      Item<String> endItem = flagList.get(i * 2 + 2);
      output += startItem.getData() + " " + startItem.getTime() + " "
          + endItem.getData() + " " + endItem.getTime() + "\n";
    }
    return output;
  }

  private Filter<TimeSeries<AccelerationData>> createFilter(
      List<TimeSeries<AccelerationData>> list
  ) {
    // return new TrueTimeSeriesFilter<AccelerationData>();
    return new MinMaxTimeSeriesFilter<AccelerationData>(this.measure, list, 1.2);
  }

  private double[] getMaxNearestNeighbourDistanceMap(
      List<TimeSeries<AccelerationData>> library,
      List<TimeSeries<AccelerationData>> gestures
  ) {
    Iterator<TimeSeries<AccelerationData>> iterator = gestures.iterator();
    double[] maxDist = new double[library.size()];
    int index = 0;
    while (iterator.hasNext()) {
      maxDist[index] = this.nnc.searchNearestNeighbour(iterator.next(), library).getDistance()
          * this.distanceBlurFactor;
      index++;
    }
    return maxDist;
  }

  private int getAverageLength(List<TimeSeries<AccelerationData>> timeSeriesList) {
    int max = 0;
    Iterator<TimeSeries<AccelerationData>> iterator = timeSeriesList.iterator();
    while (iterator.hasNext()) {
      TimeSeries<AccelerationData> timeSeries = iterator.next();
      long[] minMax = this.getMinMaxItemTime(timeSeries);
      int size = (int) (minMax[1] - minMax[0]);
      max += size;
    }
    return (int) Math.ceil((1.0 * max) / timeSeriesList.size());
  }

  private long[] getMinMaxItemTime(TimeSeries<AccelerationData> timeSeries) {
    Iterator<Item<AccelerationData>> iterator = timeSeries.getItems().iterator();
    long max = Long.MIN_VALUE;
    long min = Long.MAX_VALUE;
    while (iterator.hasNext()) {
      long time = iterator.next().getTime();
      if (time > max) {
        max = time;
      }
      if (time < min) {
        min = time;
      }
    }
    long[] minMax = new long[2];
    minMax[0] = min;
    minMax[1] = max;
    return minMax;
  }
}
