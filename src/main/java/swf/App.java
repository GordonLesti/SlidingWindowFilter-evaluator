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
import swf.measure.timeseries.AverageEstimate;
import swf.measure.timeseries.Complexity;
import swf.measure.timeseries.DynamicTimeWarping;
import swf.measure.timeseries.NormalizeDistance;
import swf.measure.timeseries.Variance;
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
    HashMap<String, swf.measure.Distance<TimeSeries<Accel>>> distHashMap = getDistances();
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
    HashMap<String, swf.measure.Distance<TimeSeries<Accel>>> hashMap = getDistances();
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
    double[] filterBlurFactors = {1.0, 1.1, 1.2, 1.3, 1.4, 1.5};
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

  private static HashMap<String, swf.measure.Distance<TimeSeries<Accel>>> getDistances() {
    HashMap<String, swf.measure.Distance<TimeSeries<Accel>>> hashMap =
        new HashMap<String, swf.measure.Distance<TimeSeries<Accel>>>();
    Complexity<Accel> complexityEstimate = new Complexity<Accel>(new EuclideanDistance());
    hashMap.put(
        "DTW",
        new DynamicTimeWarping<Accel>(new EuclideanDistance())
    );
    hashMap.put(
        "NDTW",
        new NormalizeDistance<Accel>(
            new DynamicTimeWarping<Accel>(new EuclideanDistance()),
            new Add(),
            new ScalarMult()
        )
    );
    return hashMap;
  }
}
