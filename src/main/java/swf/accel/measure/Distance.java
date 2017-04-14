package swf.accel.measure;

import swf.Accel;

public class Distance implements swf.measure.Distance<Accel> {
  /**
   * Calculates the distance between two Accel objects.
   */
  public double distance(Accel accel1, Accel accel2) {
    double diffX = accel1.getX() * 1.0 - accel2.getX();
    double diffY = accel1.getY() * 1.0 - accel2.getY();
    double diffZ = accel1.getZ() * 1.0 - accel2.getZ();
    return Math.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);
  }
}
