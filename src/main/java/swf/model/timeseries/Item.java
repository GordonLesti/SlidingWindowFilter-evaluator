package swf.model.timeseries;

import java.util.Objects;

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

  /**
   * Checks if the given object equals the Item.
   */
  public boolean equals(Object object) {
    if (object instanceof Item) {
      Item item = (Item) object;
      return this.getTime() == item.getTime() && this.getData().equals(item.getData());
    }
    return false;
  }

  public int hashCode() {
    return Objects.hash(this.getTime(), this.getData());
  }

  public String toString() {
    return "[Time: " + this.getTime() + ", Data: " + this.getData().toString() + "]";
  }
}
