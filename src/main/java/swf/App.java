package swf;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import swf.TimeSeries;
import swf.accel.io.TimeSeriesParser;
import swf.accel.measure.EuclideanDistance;
import swf.accel.operator.Add;
import swf.accel.operator.ScalarMult;
import swf.evaluation.SlidingWindow;
import swf.evaluation.slidingwindow.Threshold;
import swf.evaluation.slidingwindow.WindowSize;
import swf.evaluation.slidingwindow.threshold.HalfAverageDistance;
import swf.evaluation.slidingwindow.threshold.HalfMiddleDistance;
import swf.evaluation.slidingwindow.threshold.HalfMinDistance;
import swf.evaluation.slidingwindow.threshold.Peaking;
import swf.evaluation.slidingwindow.windowsize.Average;
import swf.evaluation.slidingwindow.windowsize.Max;
import swf.evaluation.slidingwindow.windowsize.Middle;
import swf.evaluation.slidingwindow.windowsize.Min;
import swf.filter.Factory;
import swf.filter.factory.Estimate;
import swf.filter.factory.TrueFilter;
import swf.measure.Distance;
import swf.measure.timeseries.Complexity;
import swf.measure.timeseries.DynamicTimeWarping;
import swf.measure.timeseries.LengthNormalizedEstimate;
import swf.measure.timeseries.NormalizedDistance;
import swf.measure.timeseries.Variance;
import swf.measure.timeseries.dynamictimewarping.AdjustmentWindowCondition;
import swf.measure.timeseries.dynamictimewarping.NoCondition;
import swf.measure.timeseries.dynamictimewarping.SakoeChibaBand;
import swf.nnc.factory.FullSearch;
import swf.timeseries.normalizer.ZeroMean;
import swf.timeseries.normalizer.ZeroMeanOneVariance;

public class App {
  /**
   * Starts the App.
   */
  public static void main(String[] args) {
    List<TimeSeries<Accel>> records = getRecords();
    System.out.println("\nSlidingWindow:\n");
    List<SlidingWindow> swEvaList = getSlidingWindowEvaluator(records);
    String outputFilename = "build/resources/main/swf-result.csv";
    try {
      String output = "distance;scb;filter;window;threshold;precision_μ;recall_μ;f1score_μ;"
          + "#(nnc);";
      int recordCount = records.size();
      for (int i = 0; i < recordCount; i++) {
        output += "rec" + (i + 1) + "-precision_μ;rec" + (i + 1) + "-recall_μ;rec" + (i + 1)
            + "-f1score_μ;rec" + (i + 1) + "-#(nnc);";
      }
      String[] gestureChars = {"A", "B", "C", "D", "E", "F", "G", "H"};
      for (int i = 0; i < 8; i++) {
        output += "Ges" + gestureChars[i] + "-precision;" + "Ges" + gestureChars[i] + "-recall;"
            + "Ges" + gestureChars[i] + "-f1score;";
      }
      output = output.substring(0, output.length() - 1) + "\n";
      PrintWriter writer = new PrintWriter(outputFilename, "UTF-8");
      writer.print(output);
      for (SlidingWindow swEva : swEvaList) {
        output = swEva.getDistName() + ";" + swEva.getFilterName() + ";"
            + swEva.getWindowSizeName() + ";" + swEva.getThesholdName() + ";"
            + renderDouble(swEva.getMicroPrecision(0, recordCount, 0, 8)) + ";"
            + renderDouble(swEva.getMicroRecall(0, recordCount, 0, 8)) + ";"
            + renderDouble(swEva.getMicroFscore(1, 0, recordCount, 0, 8)) + ";"
            + renderDouble(swEva.getNncCallCount(0, recordCount)) + ";";
        for (int i = 0; i < recordCount; i++) {
          output += renderDouble(swEva.getMicroPrecision(i, i + 1, 0, 8)) + ";"
              + renderDouble(swEva.getMicroRecall(i, i + 1, 0, 8)) + ";"
              + renderDouble(swEva.getMicroFscore(1, i, i + 1, 0, 8)) + ";"
              + renderDouble(swEva.getNncCallCount(i, i + 1)) + ";";
        }
        for (int i = 0; i < 8; i++) {
          output += renderDouble(swEva.getMicroPrecision(0, recordCount, i, i + 1)) + ";"
              + renderDouble(swEva.getMicroRecall(0, recordCount, i, i + 1)) + ";"
              + renderDouble(swEva.getMicroFscore(1, 0, recordCount, i, i + 1)) + ";";
        }
        output = output.substring(0, output.length() - 1) + "\n";
        writer.print(output);
      }
      writer.close();
    } catch (Exception ioEx) {
      System.out.println("Unable to write result into file " + outputFilename);
    }
  }

  private static String renderDouble(double num) {
    if (Double.isNaN(num)) {
      return "";
    }
    return Double.toString(num);
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
    LinkedList<Future<SlidingWindow>> futureList = new LinkedList<Future<SlidingWindow>>();
    int cores = Math.max(1, Runtime.getRuntime().availableProcessors() - 1);
    System.out.println("#(threads):" + cores + "\n");
    ExecutorService service = Executors.newFixedThreadPool(cores);
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
            futureList.add(
                service.submit(
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
                )
            );
          }
        }
      }
    }
    try {
      for (Future<SlidingWindow> future : futureList) {
        evaList.add(future.get());
        counter++;
        System.out.print(".");
        if (counter % 100 == 0) {
          System.out.print(" " + counter + "/" + fullSize + "\n");
        }
      }
    } catch (InterruptedException ex) {
      ex.printStackTrace();
    } catch (ExecutionException ex) {
      ex.printStackTrace();
    }
    service.shutdownNow();
    System.out.print(" " + fullSize + "/" + fullSize + "\n");
    return evaList;
  }

  private static HashMap<String, Threshold> getThresholds() {
    HashMap<String, Threshold> hashMap = new HashMap<String, Threshold>();
    hashMap.put("HAveD", new HalfAverageDistance());
    hashMap.put("HMinD", new HalfMinDistance());
    hashMap.put("HMidD", new HalfMiddleDistance());
    hashMap.put("Peak(1.1)", new Peaking(1.1));
    hashMap.put("Peak(1.2)", new Peaking(1.2));
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
      int pro = (int) Math.round(100 * (factor * 2 - 1));
      hashMap.put(
          "LNCE(" + pro + ")",
          new Estimate<TimeSeries<Accel>>(
              new LengthNormalizedEstimate<Accel>(complexityEstimate),
              factor
          )
      );
      hashMap.put(
          "VAR(" + pro + ")",
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
    hashMap.put("Ave", new Average());
    hashMap.put("Mid", new Middle());
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
          "DTW;" + conditionName,
          new DynamicTimeWarping<Accel>(new EuclideanDistance(), condition)
      );
      hashMap.put(
          "ηDTW;" + conditionName,
          new NormalizedDistance<Accel>(
              new DynamicTimeWarping<Accel>(new EuclideanDistance(), condition),
              new ZeroMean<Accel>(new Add(),  new ScalarMult())
          )
      );
      hashMap.put(
          "η'DTW;" + conditionName,
          new NormalizedDistance<Accel>(
              new DynamicTimeWarping<Accel>(new EuclideanDistance(), condition),
              new ZeroMeanOneVariance<Accel>(
                  new EuclideanDistance(),
                  new Add(),
                  new ScalarMult()
              )
          )
      );
    }
    return hashMap;
  }

  private static HashMap<String, AdjustmentWindowCondition> getConditions() {
    HashMap<String, AdjustmentWindowCondition> hashMap =
        new HashMap<String, AdjustmentWindowCondition>();
    hashMap.put("200", new NoCondition());
    double[] factors = {0.01, 0.02, 0.03, 0.04, 0.05, 0.06, 0.07, 0.08, 0.09,
        0.1, 0.11, 0.12, 0.13, 0.14, 0.15, 0.16, 0.17, 0.18, 0.19,
        0.2, 0.21, 0.22, 0.23, 0.24, 0.25, 0.26, 0.27, 0.28, 0.29,
        0.3, 0.4, 0.6, 0.8};
    for (int i = 0; i < factors.length; i++) {
      hashMap.put(
          Integer.toString((int)(200 * factors[i])),
          new SakoeChibaBand(factors[i])
      );
    }
    return hashMap;
  }
}
