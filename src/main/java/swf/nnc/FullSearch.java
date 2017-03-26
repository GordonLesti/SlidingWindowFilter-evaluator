package swf.nnc;

import java.lang.Comparable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import swf.calculator.Distance;

public class FullSearch<S, T extends Comparable<T>> implements NearestNeighbourClassificator<S, T> {
  private Distance<T, S> distanceCalculator;

  public FullSearch(Distance<T, S> distanceCalculator) {
    this.distanceCalculator = distanceCalculator;
  }

  /**
   * Calculates the nearest neighbour to a given object.
   */
  public Result<S, T> searchNearestNeighbour(S queryObject, Collection<S> objectCollection) {
    Iterator<S> iterator = objectCollection.iterator();
    S minimum = iterator.next();
    T minimumDistance = this.distanceCalculator.calculateDistance(minimum, queryObject);
    while (iterator.hasNext()) {
      S object = iterator.next();
      T distance = this.distanceCalculator.calculateDistance(object, queryObject);
      if (minimumDistance.compareTo(distance) > 0) {
        minimum = object;
        minimumDistance = distance;
      }
    }
    return new Result<S, T>(minimum, minimumDistance);
  }
}
