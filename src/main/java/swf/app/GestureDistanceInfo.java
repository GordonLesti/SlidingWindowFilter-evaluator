package swf.app;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import swf.accel.model.AccelerationData;
import swf.model.TimeSeries;
import swf.nnc.NearestNeighbourClassificator;
import swf.transformer.SubTransformer;

public class GestureDistanceInfo implements Evaluator {
  private NearestNeighbourClassificator<TimeSeries<AccelerationData>> nearestNeighbourClassificator;

  public GestureDistanceInfo(
      NearestNeighbourClassificator<TimeSeries<AccelerationData>> nearestNeighbourClassificator
  ) {
    this.nearestNeighbourClassificator = nearestNeighbourClassificator;
  }

  /**
   * Checks if the library gestures match the given gestures.
   */
  public String evaluate(List<TimeSeries<AccelerationData>> timeSeriesList) {
    Iterator<TimeSeries<AccelerationData>> iterator = timeSeriesList.iterator();
    String output = "";
    while (iterator.hasNext()) {
      output += this.evaluateTimeSeries(iterator.next()) + "\n";
    }
    return output;
  }

  private String evaluateTimeSeries(TimeSeries<AccelerationData> timeSeries) {
    LinkedList<TimeSeries<AccelerationData>> library =
        new LinkedList<TimeSeries<AccelerationData>>();
    LinkedList<TimeSeries<AccelerationData>> gestures =
        new LinkedList<TimeSeries<AccelerationData>>();
    for (int i = 1; i < 9; i++) {
      SubTransformer<AccelerationData> subTransformer =
          new SubTransformer<AccelerationData>("START " + i, "END " + i);
      library.add(subTransformer.transform(timeSeries));
      subTransformer = new SubTransformer<AccelerationData>("START " + (i + 8), "END " + (i + 8));
      gestures.add(subTransformer.transform(timeSeries));
    }
    String output = "";
    for (int i = 0; i < 8; i++) {
      TimeSeries<AccelerationData> gesture = gestures.get(i);
      output += i + " => " + library.indexOf(
          this.nearestNeighbourClassificator.searchNearestNeighbour(gesture, library)
      ) + "\n";
    }
    return output;
  }
}
