package swf.measure.timeseries;

import swf.TimeSeries;
import swf.timeseries.Point;

public class DynamicTimeWarping<T> implements swf.measure.Distance<TimeSeries<T>> {
  private swf.measure.Distance<T> distance;

  public DynamicTimeWarping(swf.measure.Distance<T> distance) {
    this.distance = distance;
  }

  /**
   * Calculates the warping distance between two time series.
   */
  public double distance(TimeSeries<T> ts1, TimeSeries<T> ts2) {
    int size1 = ts1.size();
    int size2 = ts2.size();
    double[][] matrix = new double[size1][size2];
    int index1 = 0;
    for (Point<T> p1 : ts1) {
      int index2 = 0;
      for (Point<T> p2 : ts2) {
        double cost;
        if (index1 > 0) {
          if (index2 > 0) {
            cost = Math.min(
                matrix[index1 - 1][index2],
                Math.min(
                    matrix[index1 - 1][index2 - 1],
                    matrix[index1][index2 - 1]
                )
            );
          } else {
            cost = matrix[index1 - 1][index2];
          }
        } else {
          if (index2 > 0) {
            cost = matrix[index1][index2 - 1];
          } else {
            cost = 0;
          }
        }
        matrix[index1][index2] = cost + this.distance.distance(p1.getData(), p2.getData());
        index2++;
      }
      index1++;
    }
    return matrix[size1 - 1][size2 - 1];
  }
}
