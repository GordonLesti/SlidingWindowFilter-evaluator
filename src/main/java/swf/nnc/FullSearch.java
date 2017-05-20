package swf.nnc;

import java.util.List;
import swf.measure.Distance;

public class FullSearch<T> extends NearestNeighbourClassificator<T> {
  public FullSearch(Distance<T> distance, List<T> testData) {
    super(distance, testData);
  }

  protected void preProcess() {
  }

  /**
   * Returns the first nearest neighbour for the given query.
   */
  public T nearestNeighbour(T query) {
    double min = Double.POSITIVE_INFINITY;
    T nn = null;
    for (T curr : this.testData) {
      double dist = this.distance.distance(curr, query);
      if (nn == null || dist < min) {
        nn = curr;
        min = dist;
      }
    }
    return nn;
  }
}
