package swf.app;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import swf.accel.model.AccelerationData;
import swf.model.TimeSeries;
import swf.nnc.NearestNeighbourClassificator;
import swf.transformer.SubTransformer;

public class GestureDistanceInfo implements Evaluator {
  private String title;
  private NearestNeighbourClassificator<TimeSeries<AccelerationData>> nearestNeighbourClassificator;

  public GestureDistanceInfo(
      String title,
      NearestNeighbourClassificator<TimeSeries<AccelerationData>> nearestNeighbourClassificator
  ) {
    this.title = title;
    this.nearestNeighbourClassificator = nearestNeighbourClassificator;
  }

  /**
   * Checks if the library gestures match the given gestures.
   */
  public String evaluate(List<TimeSeries<AccelerationData>> timeSeriesList) {
    Iterator<TimeSeries<AccelerationData>> iterator = timeSeriesList.iterator();
    String output = this.title + ":\n";
    String format = "| Record %d | %d | %d | %d | %d | %d | %d | %d | %d |\n";
    int recordIndex = 1;
    String top = "+----------+---+---+---+---+---+---+---+---+\n";
    output += top;
    while (iterator.hasNext()) {
      int[] result = this.evaluateTimeSeries(iterator.next());
      output += String.format(
          format,
          recordIndex,
          result[0],
          result[1],
          result[2],
          result[3],
          result[4],
          result[5],
          result[6],
          result[7]
      );
      recordIndex++;
    }
    return output + top;
  }

  private int[] evaluateTimeSeries(TimeSeries<AccelerationData> timeSeries) {
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
    int[] result = new int[8];
    for (int i = 0; i < 8; i++) {
      TimeSeries<AccelerationData> gesture = gestures.get(i);
      result[i] = library.indexOf(
          this.nearestNeighbourClassificator.searchNearestNeighbour(gesture, library)
      );
    }
    return result;
  }
}
