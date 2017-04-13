package swf.timeseries;

import java.util.Objects;

public class Point<T> {
  private T data;
  private String tag;

  public Point(T data) {
    this.data = data;
    this.tag = "";
  }

  public Point(T data, String tag) {
    this.data = data;
    this.tag = tag;
  }

  public T getData() {
    return this.data;
  }

  public String getTag() {
    return this.tag;
  }

  /**
   * Checks if the given Object equals the Point object.
   */
  public boolean equals(Object object) {
    if (object instanceof Point) {
      Point dp = (Point) object;
      return this.getData().equals(dp.getData()) && this.getTag().equals(dp.getTag());
    }
    return false;
  }

  public int hashCode() {
    return Objects.hash(this.getData(), this.getTag());
  }

  /**
   * Returns a String representing the Point.
   */
  public String toString() {
    String rep = this.getData().toString();
    if (!this.getTag().equals("")) {
      rep += " (" + this.getTag() + ")";
    }
    return rep;
  }
}
