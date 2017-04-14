package swf;

import java.util.LinkedList;
import java.util.List;
import swf.TimeSeries;
import swf.accel.io.TimeSeriesParser;
import swf.accel.measure.Distance;
import swf.measure.timeseries.DynamicTimeWarping;
import swf.nnc.factory.FullSearch;

public class App {
  /**
   * Starts the App.
   */
  public static void main(String[] args) {
    for (swf.evaluation.Distance distEva : getDistanceEvaluator(getRecords())) {
      System.out.println(distEva.getName() + " " + distEva.getSuccessQuotient());
    }
  }

  private static List<TimeSeries<Accel>> getRecords() {
    LinkedList<TimeSeries<Accel>> tsList = new LinkedList<TimeSeries<Accel>>();
    TimeSeriesParser tsp = new TimeSeriesParser();
    for (int i = 1; i < 11; i++) {
      tsList.add(tsp.parseFile("build/resources/main/record" + i + ".txt"));
    }
    return tsList;
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
            "DTW"
        )
    );
    return evaList;
  }
}
