package swf.accel.operator;

import swf.Accel;

public class ScalarMult implements swf.operator.ScalarMult<Accel> {
  /**
   * Multiplies the Accel data with a scalar.
   */
  public Accel mult(double scalar, Accel accel) {
    return new Accel(
      (int) Math.round(scalar * accel.getX()),
      (int) Math.round(scalar * accel.getY()),
      (int) Math.round(scalar * accel.getZ())
    );
  }
}
