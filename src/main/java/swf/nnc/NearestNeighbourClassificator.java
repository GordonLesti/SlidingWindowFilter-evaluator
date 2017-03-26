package swf.nnc;

import java.util.Collection;

public interface NearestNeighbourClassificator<S, T> {
  public Result<S, T> searchNearestNeighbour(S queryObject, Collection<S> objectCollection);
}
