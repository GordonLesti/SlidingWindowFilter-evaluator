package swf.calculator.distance;

import java.util.Iterator;
import java.util.List;
import swf.calculator.Distance;
import swf.model.TimeSeries;
import swf.model.timeseries.Item;

public class DynamicTimeWarping<U> implements Distance<Double, TimeSeries<U>> {
  private Distance<Double, U> distanceCalculator;

  public DynamicTimeWarping(Distance<Double, U> distanceCalculator) {
    this.distanceCalculator = distanceCalculator;
  }

  /**
   * Calculates the classical Dynamic Time Warping distance between two TimeSeries.
   */
  public Double calculateDistance(TimeSeries<U> timeSeries1, TimeSeries<U> timeSeries2) {
    List<Item<U>> items1 = timeSeries1.getItems();
    List<Item<U>> items2 = timeSeries2.getItems();
    int size1 = items1.size();
    int size2 = items2.size();
    int counter1 = 0;
    double[][] matrix = new double[size1][size2];
    Iterator<Item<U>> iterator1 = items1.iterator();
    while (iterator1.hasNext()) {
      Item<U> item1 = iterator1.next();
      Iterator<Item<U>> iterator2 = items2.iterator();
      int counter2 = 0;
      while (iterator2.hasNext()) {
        Item<U> item2 = iterator2.next();
        this.setCost(counter1, counter2, item1.getData(), item2.getData(), matrix);
        counter2++;
      }
      counter1++;
    }
    return matrix[size1 - 1][size2 - 1];
  }

  private void setCost(int indexI, int indexJ, U dataI, U dataJ, double[][] matrix) {
    double cost;
    if (indexI > 0) {
      if (indexJ > 0) {
        cost = Math.min(
            matrix[indexI - 1][indexJ - 1],
            Math.min(matrix[indexI - 1][indexJ], matrix[indexI][indexJ - 1])
        );
      } else {
        cost = matrix[indexI - 1][indexJ];
      }
    } else {
      if (indexJ > 0) {
        cost = matrix[indexI][indexJ - 1];
      } else {
        cost = 0;
      }
    }
    matrix[indexI][indexJ] = cost + this.distanceCalculator.calculateDistance(dataI, dataJ);
  }
}
