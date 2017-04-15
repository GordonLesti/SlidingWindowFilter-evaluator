package swf;

import java.util.LinkedList;
import java.util.List;
import swf.TimeSeries;
import swf.accel.io.TimeSeriesParser;
import swf.accel.measure.Distance;
import swf.accel.operator.Add;
import swf.accel.operator.ScalarMult;
import swf.evaluation.SlidingWindow;
import swf.evaluation.slidingwindow.threshold.MinDistance;
import swf.evaluation.slidingwindow.windowsize.Average;
import swf.evaluation.slidingwindow.windowsize.Max;
import swf.evaluation.slidingwindow.windowsize.Min;
import swf.measure.MultiplyDistance;
import swf.measure.timeseries.Complexity;
import swf.measure.timeseries.DynamicTimeWarping;
import swf.measure.timeseries.MaxMinQuotient;
import swf.measure.timeseries.NormalizeDistance;
import swf.nnc.factory.FullSearch;

public class App {
  /**
   * Starts the App.
   */
  public static void main(String[] args) {
    List<TimeSeries<Accel>> records = getRecords();
    for (swf.evaluation.Distance distEva : getDistanceEvaluator(records)) {
      System.out.println(distEva.getName() + " " + distEva.getSuccessQuotient());
    }
    for (SlidingWindow swEva : getSlidingWindowEvaluator(records)) {
      System.out.println(swEva.getName() + " " + swEva.getSuccessCount() + " " + swEva.getFailCount());
    }
  }

  private static List<TimeSeries<Accel>> getRecords() {
    LinkedList<TimeSeries<Accel>> tsList = new LinkedList<TimeSeries<Accel>>();
    TimeSeriesParser tsp = new TimeSeriesParser();
    for (int i = 1; i < 2; i++) {
      tsList.add(tsp.parseFile("build/resources/main/record" + i + ".txt"));
    }
    return tsList;
  }

  private static List<SlidingWindow> getSlidingWindowEvaluator(
      List<TimeSeries<Accel>> tsList
  ) {
    LinkedList<SlidingWindow> evaList = new LinkedList<SlidingWindow>();
    evaList.add(
        new SlidingWindow(
            tsList,
            new DynamicTimeWarping<Accel>(new Distance()),
            new FullSearch<TimeSeries<Accel>>(),
            new Average(),
            new MinDistance(),
            "DynamicTimeWarping Average"
        )
    );
    evaList.add(
        new SlidingWindow(
            tsList,
            new DynamicTimeWarping<Accel>(new Distance()),
            new FullSearch<TimeSeries<Accel>>(),
            new Max(),
            new MinDistance(),
            "DynamicTimeWarping Max"
        )
    );
    evaList.add(
        new SlidingWindow(
            tsList,
            new DynamicTimeWarping<Accel>(new Distance()),
            new FullSearch<TimeSeries<Accel>>(),
            new Min(),
            new MinDistance(),
            "DynamicTimeWarping Min"
        )
    );
    evaList.add(
        new SlidingWindow(
            tsList,
            new NormalizeDistance<Accel>(
                new DynamicTimeWarping<Accel>(new Distance()),
                new Add(),
                new ScalarMult()
            ),
            new FullSearch<TimeSeries<Accel>>(),
            new Average(),
            new MinDistance(),
            "Normalized DynamicTimeWarping"
        )
    );
    return evaList;
  }

  private static List<swf.evaluation.Distance> getDistanceEvaluator(
      List<TimeSeries<Accel>> tsList
  ) {
    LinkedList<swf.evaluation.Distance> evaList = new LinkedList<swf.evaluation.Distance>();
    evaList.add(
        new swf.evaluation.Distance(
            tsList,
            new DynamicTimeWarping<Accel>(new Distance()),
            new FullSearch<TimeSeries<Accel>>(),
            "DynamicTimeWarping"
        )
    );
    evaList.add(
        new swf.evaluation.Distance(
            tsList,
            new NormalizeDistance<Accel>(
                new DynamicTimeWarping<Accel>(new Distance()),
                new Add(),
                new ScalarMult()
            ),
            new FullSearch<TimeSeries<Accel>>(),
            "Normalized DynamicTimeWarping"
        )
    );
    evaList.add(
        new swf.evaluation.Distance(
            tsList,
            new MaxMinQuotient<Accel>(new Complexity<Accel>(new Distance())),
            new FullSearch<TimeSeries<Accel>>(),
            "Complexity"
        )
    );
    evaList.add(
        new swf.evaluation.Distance(
            tsList,
            new MultiplyDistance<TimeSeries<Accel>>(
                new MaxMinQuotient<Accel>(new Complexity<Accel>(new Distance())),
                new DynamicTimeWarping<Accel>(new Distance())
            ),
            new FullSearch<TimeSeries<Accel>>(),
            "Complexity DynamicTimeWarping"
        )
    );
    return evaList;
  }
}
