package swf.nnc;

import java.util.List;
import swf.measure.Distance;

public abstract class NearestNeighbourClassificator<T> {
  protected Distance<T> distance;
  protected List<T> testData;

  /**
   * Creates a new instance of a nearest neighbour classificator.
   */
  public NearestNeighbourClassificator(Distance<T> distance, List<T> testData) {
    this.distance = distance;
    this.testData = testData;
    this.preProcess();
  }

  protected abstract void preProcess();

  public abstract T nearestNeighbour(T query);

  public Distance<T> getDistance() {
    return this.distance;
  }
}
