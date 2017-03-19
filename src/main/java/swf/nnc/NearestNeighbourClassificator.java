package swf.nnc;

import java.util.Collection;

public interface NearestNeighbourClassificator<S> {
  public S searchNearestNeighbour(S queryObject, Collection<S> objectCollection);
}
