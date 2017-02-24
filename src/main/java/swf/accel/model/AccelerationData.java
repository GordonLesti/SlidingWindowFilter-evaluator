package swf.accel.model;

import java.util.Objects;

public class AccelerationData {
  private int axisX;
  private int axisY;
  private int axisZ;

  /**
   * Creates a new AccelerationData object.
   */
  public AccelerationData(int axisX, int axisY, int axisZ) {
    this.axisX = axisX;
    this.axisY = axisY;
    this.axisZ = axisZ;
  }

  public int getX() {
    return this.axisX;
  }

  public int getY() {
    return this.axisY;
  }

  public int getZ() {
    return this.axisZ;
  }

  /**
   * Checks if the given object equals the AccelerationData.
   */
  public boolean equals(Object object) {
    if (object instanceof AccelerationData) {
      AccelerationData accel = (AccelerationData) object;
      return this.getX() == accel.getX()
          && this.getY() == accel.getY()
          && this.getZ() == accel.getZ();
    }
    return false;
  }

  public int hashCode() {
    return Objects.hash(this.getX(), this.getY(), this.getZ());
  }

  public String toString() {
    return "[" + this.getX() + ", " + this.getY() + ", " + this.getZ() + "]";
  }
}
