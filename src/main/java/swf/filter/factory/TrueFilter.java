package swf.filter.factory;

import java.util.Collection;
import swf.filter.Factory;
import swf.filter.Filter;

public class TrueFilter<T> implements Factory<T> {
  public Filter<T> create(Collection<T> collection) {
    return new swf.filter.TrueFilter<T>();
  }
}
