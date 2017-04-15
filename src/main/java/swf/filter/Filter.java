package swf.filter;

public interface Filter<T> {
  public boolean filter(T obj);
}
