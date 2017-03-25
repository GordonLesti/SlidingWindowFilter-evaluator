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
    String filename = "build/resources/main/record2.txt";
    try {
      TimeSeries<AccelerationData> timeSeries =
          chainTransformer.transform(timeSeriesParser.parseTimeSeriesFromFile(filename));
      Distance distance = new Distance();
      DynamicTimeWarping<AccelerationData> dtw =
          new DynamicTimeWarping<AccelerationData>(distance);
      MaxMinQuotient<TimeSeries<AccelerationData>> complexFactor =
          new MaxMinQuotient<TimeSeries<AccelerationData>>(
              new Complexity<AccelerationData>(distance)
          );
      MultiplyDistance<TimeSeries<AccelerationData>> complexDtw =
          new MultiplyDistance<TimeSeries<AccelerationData>>(dtw, complexFactor);
      GestureDistanceInfo dtwGestureDistanceInfo =
          new GestureDistanceInfo("DTW", new FullSearch<TimeSeries<AccelerationData>, Double>(dtw));
      GestureDistanceInfo complexDtwGestureDistanceInfo =
          new GestureDistanceInfo(
              "complexDTW",
              new FullSearch<TimeSeries<AccelerationData>, Double>(complexDtw)
          );
      LinkedList<TimeSeries<AccelerationData>> list =
          new LinkedList<TimeSeries<AccelerationData>>();
      for (int i = 1; i < 6; i++) {
        list.add(
            chainTransformer.transform(
                timeSeriesParser.parseTimeSeriesFromFile("build/resources/main/record" + i + ".txt")
            )
        );
      }
      System.out.println(dtwGestureDistanceInfo.evaluate(list));
      System.out.println(complexDtwGestureDistanceInfo.evaluate(list));
    } catch (FileNotFoundException fnfe) {
      System.out.println("Can not find file " + filename);
    } catch (IOException ioe) {
      System.out.println("Problems while reading file " + filename);
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
