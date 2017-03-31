package swf;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import swf.accel.calculator.Distance;
import swf.accel.calculator.Mean;
import swf.accel.io.TimeSeriesParser;
import swf.accel.model.AccelerationData;
import swf.accel.transformer.QuantizeTransformer;
import swf.app.evaluator.GestureDistanceInfo;
import swf.calculator.distance.DynamicTimeWarping;
import swf.calculator.distance.MaxMinQuotient;
import swf.calculator.distance.MultiplyDistance;
import swf.calculator.measure.AverageComplexity;
import swf.calculator.measure.Complexity;
import swf.model.TimeSeries;
import swf.nnc.FullSearch;
import swf.transformer.ChainTransformer;
import swf.transformer.MeanTransformer;
import swf.transformer.SubTransformer;
import swf.transformer.TimeSeriesTransformer;

public class App {
  /**
   * Starts the App.
   */
  public static void main(String[] args) {
    TimeSeriesParser timeSeriesParser = new TimeSeriesParser();
    TimeSeriesTransformer<AccelerationData> chainTransformer = createTransformer();
    try {
      Distance distance = new Distance();
      DynamicTimeWarping<AccelerationData> dtw =
          new DynamicTimeWarping<AccelerationData>(distance);
      Complexity<AccelerationData> complexity = new Complexity<AccelerationData>(distance);
      MaxMinQuotient<TimeSeries<AccelerationData>> complexFactor =
          new MaxMinQuotient<TimeSeries<AccelerationData>>(complexity);
      MaxMinQuotient<TimeSeries<AccelerationData>> averageComplexFactor =
          new MaxMinQuotient<TimeSeries<AccelerationData>>(
              new AverageComplexity<AccelerationData>(complexity)
          );
      MultiplyDistance<TimeSeries<AccelerationData>> complexDtw =
          new MultiplyDistance<TimeSeries<AccelerationData>>(dtw, complexFactor);
      MultiplyDistance<TimeSeries<AccelerationData>> averageComplexDTW =
          new MultiplyDistance<TimeSeries<AccelerationData>>(dtw, averageComplexFactor);
      GestureDistanceInfo dtwGestureDistanceInfo =
          new GestureDistanceInfo("DTW", new FullSearch<TimeSeries<AccelerationData>, Double>(dtw));
      GestureDistanceInfo complexDtwGestureDistanceInfo =
          new GestureDistanceInfo(
              "complexDTW",
              new FullSearch<TimeSeries<AccelerationData>, Double>(complexDtw)
          );
      GestureDistanceInfo averageComplexDtwGestureDistInfo =
        new GestureDistanceInfo(
            "averageComplexDTW",
            new FullSearch<TimeSeries<AccelerationData>, Double>(averageComplexDTW)
        );
      LinkedList<TimeSeries<AccelerationData>> list =
          new LinkedList<TimeSeries<AccelerationData>>();
      for (int i = 1; i < 8; i++) {
        list.add(
            chainTransformer.transform(
                timeSeriesParser.parseTimeSeriesFromFile("build/resources/main/record" + i + ".txt")
            )
        );
      }
      System.out.println(dtwGestureDistanceInfo.evaluate(list));
      System.out.println(complexDtwGestureDistanceInfo.evaluate(list));
      System.out.println(averageComplexDtwGestureDistInfo.evaluate(list));
    } catch (FileNotFoundException fnfe) {
      System.out.println(fnfe.getMessage());
    } catch (IOException ioe) {
      System.out.println(ioe.getMessage());
    }
  }

  private static TimeSeriesTransformer<AccelerationData> createTransformer() {
    QuantizeTransformer quantizeTransformer = new QuantizeTransformer();
    MeanTransformer<AccelerationData> meanTransformer =
        new MeanTransformer<AccelerationData>(50, 30, new Mean());
    SubTransformer<AccelerationData> subTrans =
        new SubTransformer<AccelerationData>("END 0", "START 17");
    LinkedList<TimeSeriesTransformer<AccelerationData>> transformerList =
        new LinkedList<TimeSeriesTransformer<AccelerationData>>();
    transformerList.add(subTrans);
    transformerList.add(meanTransformer);
    transformerList.add(quantizeTransformer);
    return new ChainTransformer<AccelerationData>(transformerList);
  }
}
