package swf.model;

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
}
