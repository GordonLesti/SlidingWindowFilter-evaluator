package swf;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import swf.accel.calculator.Distance;
import swf.accel.calculator.Mean;
import swf.accel.calculator.Subtraction;
import swf.accel.io.TimeSeriesParser;
import swf.accel.model.AccelerationData;
import swf.accel.transformer.QuantizeTransformer;
import swf.app.Evaluator;
import swf.app.evaluator.GestureDistanceInfo;
import swf.app.evaluator.SlidingWindowFilter;
import swf.calculator.distance.DynamicTimeWarping;
import swf.calculator.distance.MaxMinQuotient;
import swf.calculator.distance.MultiplyDistance;
import swf.calculator.distance.NormalizeDynamicTimeWarping;
import swf.calculator.measure.AverageComplexity;
import swf.calculator.measure.Complexity;
import swf.model.TimeSeries;
import swf.nnc.FullSearch;
import swf.transformer.ChainTransformer;
import swf.transformer.MeanTransformer;
import swf.transformer.NormalizeTransformer;
import swf.transformer.SubFlagTransformer;
import swf.transformer.TimeSeriesTransformer;

public class App {
  /**
   * Starts the App.
   */
  public static void main(String[] args) {
    TimeSeriesParser timeSeriesParser = new TimeSeriesParser();
    TimeSeriesTransformer<AccelerationData> chainTransformer = createTransformer();
    try {
      LinkedList<TimeSeries<AccelerationData>> list =
          new LinkedList<TimeSeries<AccelerationData>>();
      for (int i = 1; i < 10; i++) {
        list.add(
            chainTransformer.transform(
                timeSeriesParser.parseTimeSeriesFromFile("build/resources/main/record" + i + ".txt")
            )
        );
      }
      Evaluator[] evaluators = createEvaluators();
      for (int i = 0; i < evaluators.length; i++) {
        System.out.println(evaluators[i].evaluate(list));
      }
    } catch (FileNotFoundException fnfe) {
      System.out.println(fnfe.getMessage());
    } catch (IOException ioe) {
      System.out.println(ioe.getMessage());
    }
  }

  private static Distance createDistance() {
    return new Distance();
  }

  private static DynamicTimeWarping<AccelerationData> createDtw() {
    return new DynamicTimeWarping<AccelerationData>(createDistance());
  }

  private static NormalizeDynamicTimeWarping<AccelerationData> createNormalizeDtw() {
    return new NormalizeDynamicTimeWarping<AccelerationData>(
        createDtw(),
        new NormalizeTransformer<AccelerationData>(
            new Subtraction(),
            new Mean()
        )
    );
  }

  private static Complexity<AccelerationData> createComplexity() {
    return new Complexity<AccelerationData>(createDistance());
  }

  private static MaxMinQuotient<TimeSeries<AccelerationData>> createComplexFactor() {
    return new MaxMinQuotient<TimeSeries<AccelerationData>>(createComplexity());
  }

  private static MaxMinQuotient<TimeSeries<AccelerationData>> createAverageComplexFactor() {
    return new MaxMinQuotient<TimeSeries<AccelerationData>>(
        new AverageComplexity<AccelerationData>(createComplexity())
    );
  }

  private static MultiplyDistance<TimeSeries<AccelerationData>> createComplexDtw() {
    return new MultiplyDistance<TimeSeries<AccelerationData>>(createDtw(), createComplexFactor());
  }

  private static MultiplyDistance<TimeSeries<AccelerationData>> createAverageComplexDtw() {
    return new MultiplyDistance<TimeSeries<AccelerationData>>(
        createDtw(),
        createAverageComplexFactor()
    );
  }

  private static GestureDistanceInfo createDtwGestureInfo() {
    return new GestureDistanceInfo(
        "DTW",
        new FullSearch<TimeSeries<AccelerationData>, Double>(createDtw())
    );
  }

  private static GestureDistanceInfo createNormalizeDtwGestureInfo() {
    return new GestureDistanceInfo(
        "NormalizeDTW",
        new FullSearch<TimeSeries<AccelerationData>, Double>(createNormalizeDtw())
    );
  }

  private static GestureDistanceInfo createComplexDtwGestureInfo() {
    return new GestureDistanceInfo(
        "complexDTW",
        new FullSearch<TimeSeries<AccelerationData>, Double>(createComplexDtw())
    );
  }

  private static GestureDistanceInfo createAverageComplexDtwGestureInfo() {
    return new GestureDistanceInfo(
        "averageComplexDTW",
        new FullSearch<TimeSeries<AccelerationData>, Double>(createAverageComplexDtw())
    );
  }

  private static SlidingWindowFilter createSlidingWindowFilter() {
    return new SlidingWindowFilter(
        createComplexity(),
        new FullSearch<TimeSeries<AccelerationData>, Double>(createNormalizeDtw()),
        0.1,
        1.2
    );
  }

  private static Evaluator[] createEvaluators() {
    Evaluator[] evaluators = new Evaluator[5];
    evaluators[0] = createDtwGestureInfo();
    evaluators[1] = createNormalizeDtwGestureInfo();
    evaluators[2] = createComplexDtwGestureInfo();
    evaluators[3] = createAverageComplexDtwGestureInfo();
    evaluators[4] = createSlidingWindowFilter();
    return evaluators;
  }

  private static TimeSeriesTransformer<AccelerationData> createTransformer() {
    QuantizeTransformer quantizeTransformer = new QuantizeTransformer();
    MeanTransformer<AccelerationData> meanTransformer =
        new MeanTransformer<AccelerationData>(50, 30, new Mean());
    SubFlagTransformer<AccelerationData> subTrans =
        new SubFlagTransformer<AccelerationData>("END 0", "START 17");
    LinkedList<TimeSeriesTransformer<AccelerationData>> transformerList =
        new LinkedList<TimeSeriesTransformer<AccelerationData>>();
    transformerList.add(subTrans);
    transformerList.add(meanTransformer);
    transformerList.add(quantizeTransformer);
    return new ChainTransformer<AccelerationData>(transformerList);
  }
}
