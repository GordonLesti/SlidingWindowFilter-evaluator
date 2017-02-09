package swf.model.timeseries;

public class Item<T> {

  private long time;

  private T data;

  public Item(long time, T data) {
    this.time = time;
    this.data = data;
  }

  public long getTime() {
    return this.time;
  }

  public T getData() {
    return this.data;
  }
}
