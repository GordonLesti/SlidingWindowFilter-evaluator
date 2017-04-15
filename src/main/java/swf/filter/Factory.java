package swf.filter;

import java.util.Collection;
import swf.filter.Filter;

public interface Factory<T> {
  public Filter<T> create(Collection<T> collection);
}
