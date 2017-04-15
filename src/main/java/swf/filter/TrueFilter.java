package swf.filter;

public class TrueFilter<T> implements Filter<T> {
  public boolean filter(T obj) {
    return true;
  }
}
