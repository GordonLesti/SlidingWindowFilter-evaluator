package swf.nnc.factory;

import java.util.List;
import swf.measure.Distance;
import swf.nnc.Factory;
import swf.nnc.NearestNeighbourClassificator;

public class FullSearch<T> implements Factory<T> {
  public NearestNeighbourClassificator<T> create(Distance<T> distance, List<T> testData) {
    return new swf.nnc.FullSearch<T>(distance, testData);
  }
}
