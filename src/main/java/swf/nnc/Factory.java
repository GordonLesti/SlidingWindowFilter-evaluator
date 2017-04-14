package swf.nnc;

import java.util.List;
import swf.measure.Distance;

public interface Factory<T> {
  public NearestNeighbourClassificator<T> create(Distance<T> distance, List<T> testData);
}
