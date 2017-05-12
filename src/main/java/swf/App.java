package swf;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import swf.TimeSeries;
import swf.accel.io.TimeSeriesParser;
import swf.accel.measure.EuclideanDistance;
import swf.accel.operator.Add;
import swf.accel.operator.ScalarMult;
import swf.evaluation.SlidingWindow;
import swf.evaluation.slidingwindow.Threshold;
import swf.evaluation.slidingwindow.WindowSize;
import swf.evaluation.slidingwindow.threshold.Cheating;
import swf.evaluation.slidingwindow.threshold.HalfAverageDistance;
import swf.evaluation.slidingwindow.threshold.HalfMiddleDistance;
import swf.evaluation.slidingwindow.threshold.HalfMinDistance;
import swf.evaluation.slidingwindow.windowsize.Average;
import swf.evaluation.slidingwindow.windowsize.Max;
import swf.evaluation.slidingwindow.windowsize.Middle;
import swf.evaluation.slidingwindow.windowsize.Min;
import swf.filter.Factory;
import swf.filter.factory.Estimate;
import swf.filter.factory.TrueFilter;
import swf.measure.Distance;
import swf.measure.timeseries.AverageEstimate;
import swf.measure.timeseries.Complexity;
import swf.measure.timeseries.DynamicTimeWarping;
import swf.measure.timeseries.NormalizeDistance;
import swf.measure.timeseries.Variance;
import swf.measure.timeseries.dynamictimewarping.AdjustmentWindowCondition;
import swf.measure.timeseries.dynamictimewarping.NoCondition;
import swf.measure.timeseries.dynamictimewarping.SakoeChibaBand;
import swf.nnc.factory.FullSearch;

public class App {
  /**
   * Starts the App.
   */
  public static void main(String[] args) {
    List<TimeSeries<Accel>> records = getRecords();
    System.out.println("Distance:\n");
    List<swf.evaluation.Distance> distEvaList = getDistanceEvaluator(records);
    Collections.sort(distEvaList);
    Collections.reverse(distEvaList);
    for (swf.evaluation.Distance distEva : distEvaList) {
      System.out.println(distEva.getName() + " " + distEva.getSuccessQuotient());
    }
    System.out.println("\nSlidingWindow:\n");
    List<SlidingWindow> swEvaList = getSlidingWindowEvaluator(records);
    Collections.sort(swEvaList);
    Collections.reverse(swEvaList);
    String output = "distance;filter;window;threshold;precision;recall;f1score;accuracy;#(nnc)\n";
    for (SlidingWindow swEva : swEvaList) {
      output += swEva.getDistName() + ";" + swEva.getFilterName() + ";"
          + swEva.getWindowSizeName() + ";" + swEva.getThesholdName() + ";"
          + swEva.getMicroPrecision() + ";" + swEva.getMicroRecall() + ";"
          + swEva.getFscore(1) + ";" + swEva.getAverageAccuracy()
          + ";" + swEva.getNncCallCount() + "\n";
    }
    String outputFilename = "build/resources/main/swf-result.csv";
    try {
      PrintWriter writer = new PrintWriter(outputFilename, "UTF-8");
      writer.print(output);
      writer.close();
    } catch (Exception ioEx) {
      System.out.println("Unable to write result into file " + outputFilename);
    }
  }

  private static List<TimeSeries<Accel>> getRecords() {
    LinkedList<TimeSeries<Accel>> tsList = new LinkedList<TimeSeries<Accel>>();
    TimeSeriesParser tsp = new TimeSeriesParser();
    for (int i = 1; i < 15; i++) {
      tsList.add(tsp.parseFile("build/resources/main/record" + i + ".txt"));
    }
    return tsList;
  }

  private static List<SlidingWindow> getSlidingWindowEvaluator(
      List<TimeSeries<Accel>> tsList
  ) {
    LinkedList<SlidingWindow> evaList = new LinkedList<SlidingWindow>();
    HashMap<String, Distance<TimeSeries<Accel>>> distHashMap = getDistances();
    HashMap<String, WindowSize> windowSizeHashMap = getWindowSizes();
    HashMap<String, Factory<TimeSeries<Accel>>> filterHashMap = getFilters();
    HashMap<String, Threshold> thresholdHashMap = getThresholds();
    int fullSize = distHashMap.size() * windowSizeHashMap.size() * filterHashMap.size()
        * thresholdHashMap.size();
    int counter = 0;
    for (String distName : distHashMap.keySet()) {
      for (String wsName : windowSizeHashMap.keySet()) {
        for (String filterName : filterHashMap.keySet()) {
          for (String thresholdName : thresholdHashMap.keySet()) {
            evaList.add(
                new SlidingWindow(
                    tsList,
                    distHashMap.get(distName),
                    new FullSearch<TimeSeries<Accel>>(),
                    filterHashMap.get(filterName),
                    windowSizeHashMap.get(wsName),
                    thresholdHashMap.get(thresholdName),
                    distName,
                    filterName,
                    wsName,
                    thresholdName
                )
            );
            counter++;
            System.out.print(counter + "/" + fullSize + "\r");
          }
        }
      }
    }
    System.out.println(fullSize + "/" + fullSize);
    return evaList;
  }

  private static List<swf.evaluation.Distance> getDistanceEvaluator(
      List<TimeSeries<Accel>> tsList
  ) {
    LinkedList<swf.evaluation.Distance> evaList = new LinkedList<swf.evaluation.Distance>();
    HashMap<String, Distance<TimeSeries<Accel>>> hashMap = getDistances();
    for (String name : hashMap.keySet()) {
      evaList.add(
          new swf.evaluation.Distance(
              tsList,
              hashMap.get(name),
              new FullSearch<TimeSeries<Accel>>(),
              name
          )
      );
    }
    return evaList;
  }

  private static HashMap<String, Threshold> getThresholds() {
    HashMap<String, Threshold> hashMap = new HashMap<String, Threshold>();
    hashMap.put("HalfAverageDistance", new HalfAverageDistance());
    hashMap.put("HalfMinDistance", new HalfMinDistance());
    hashMap.put("HalfMiddleDistance", new HalfMiddleDistance());
    hashMap.put("Cheating(1.1)", new Cheating(1.1));
    hashMap.put("Cheating(1.2)", new Cheating(1.2));
    return hashMap;
  }

  private static HashMap<String, Factory<TimeSeries<Accel>>> getFilters() {
    HashMap<String, Factory<TimeSeries<Accel>>> hashMap =
        new HashMap<String, Factory<TimeSeries<Accel>>>();
    hashMap.put("NoFilter", new TrueFilter<TimeSeries<Accel>>());
    double[] filterBlurFactors = {1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2.0};
    Complexity<Accel> complexityEstimate = new Complexity<Accel>(new EuclideanDistance());
    for (int i = 0; i < filterBlurFactors.length; i++) {
      double factor = filterBlurFactors[i];
      hashMap.put(
          "CF(" + factor + ")",
          new Estimate<TimeSeries<Accel>>(complexityEstimate, factor)
      );
      hashMap.put(
          "ACF(" + factor + ")",
          new Estimate<TimeSeries<Accel>>(
              new AverageEstimate<Accel>(complexityEstimate),
              factor
          )
      );
      hashMap.put(
          "VF(" + factor + ")",
          new Estimate<TimeSeries<Accel>>(
              new Variance<Accel>(
                  new EuclideanDistance(),
                  new Add(),
                  new ScalarMult()
              ),
              factor
          )
      );
    }
    return hashMap;
  }

  private static HashMap<String, WindowSize> getWindowSizes() {
    HashMap<String, WindowSize> hashMap = new HashMap<String, WindowSize>();
    hashMap.put("Max", new Max());
    hashMap.put("Min", new Min());
    hashMap.put("Average", new Average());
    hashMap.put("Middle", new Middle());
    return hashMap;
  }

  private static HashMap<String, Distance<TimeSeries<Accel>>> getDistances() {
    HashMap<String, Distance<TimeSeries<Accel>>> hashMap =
        new HashMap<String, Distance<TimeSeries<Accel>>>();
    Complexity<Accel> complexityEstimate = new Complexity<Accel>(new EuclideanDistance());
    HashMap<String, AdjustmentWindowCondition> conditions = getConditions();
    for (String conditionName : conditions.keySet()) {
      AdjustmentWindowCondition condition = conditions.get(conditionName);
      hashMap.put(
          "DTW " + conditionName,
          new DynamicTimeWarping<Accel>(new EuclideanDistance(), condition)
      );
      hashMap.put(
          "NDTW " + conditionName,
          new NormalizeDistance<Accel>(
              new DynamicTimeWarping<Accel>(new EuclideanDistance(), condition),
              new Add(),
              new ScalarMult()
          )
      );
    }
    return hashMap;
  }

  private static HashMap<String, AdjustmentWindowCondition> getConditions() {
    HashMap<String, AdjustmentWindowCondition> hashMap =
        new HashMap<String, AdjustmentWindowCondition>();
    hashMap.put("", new NoCondition());
    double[] factors = {0.01, 0.02, 0.03, 0.04, 0.05, 0.06, 0.07, 0.08, 0.09,
        0.1, 0.11, 0.12, 0.13, 0.14, 0.15, 0.16, 0.17, 0.18, 0.19,
        0.2, 0.21, 0.22, 0.23, 0.24, 0.25, 0.26, 0.27, 0.28, 0.29,
        0.3, 0.4, 0.6, 0.8};
    for (int i = 0; i < factors.length; i++) {
      hashMap.put(
          "SCB(" + Double.toString(factors[i]) + ")",
          new SakoeChibaBand(factors[i])
      );
    }
    return hashMap;
  }
}
