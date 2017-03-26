package swf.nnc;

import java.util.Objects;

public class Result<S, T> {
  private S object;
  private T distance;

  public Result(S object, T distance) {
    this.object = object;
    this.distance = distance;
  }

  public S getObject() {
    return this.object;
  }

  public T getDistance() {
    return this.distance;
  }

  /**
   * Checks if a given objects equals the current Resutl object.
   */
  public boolean equals(Object object) {
    if (object instanceof Result) {
      Result result = (Result) object;
      return this.getObject().equals(result.getObject())
          && this.getDistance().equals(result.getDistance());
    }
    return false;
  }

  public int hashCode() {
    return Objects.hash(this.getObject(), this.getDistance());
  }
}
