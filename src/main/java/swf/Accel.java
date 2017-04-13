package swf;

import java.util.Objects;

public class Accel {
  private int axisX;
  private int axisY;
  private int axisZ;

  /**
   * Creates Accel object.
   */
  public Accel(int axisX, int axisY, int axisZ) {
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
   * Checks if the given Object equals the Accel object.
   */
  public boolean equals(Object object) {
    if (object instanceof Accel) {
      Accel accel = (Accel) object;
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
    return "(" + this.getX() + "," + this.getY() + "," + this.getZ() + ")";
  }
}
