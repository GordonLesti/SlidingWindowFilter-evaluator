package swf.accel.calculator;

import swf.accel.model.AccelerationData;

public class Distance implements swf.calculator.Distance<Double, AccelerationData> {
  /**
   * Calculates the euclidian distance between two AccelerationData objects.
   */
  public Double calculateDistance(AccelerationData acc1, AccelerationData acc2) {
    double diffX = acc1.getX() * 1.0 - acc2.getX();
    double diffY = acc1.getY() * 1.0 - acc2.getY();
    double diffZ = acc1.getZ() * 1.0 - acc2.getZ();
    return Math.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);
  }
}