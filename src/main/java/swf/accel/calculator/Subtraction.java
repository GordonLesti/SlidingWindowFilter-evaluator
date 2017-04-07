package swf.accel.calculator;

import swf.accel.model.AccelerationData;

public class Subtraction implements swf.calculator.Subtraction<AccelerationData> {
  /**
   * Calculates the difference of minuend and subtrahend.
   */
  public AccelerationData subtract(AccelerationData minuend, AccelerationData subtrahend) {
    return new AccelerationData(
      minuend.getX() - subtrahend.getX(),
      minuend.getY() - subtrahend.getY(),
      minuend.getZ() - subtrahend.getZ()
    );
  }
}
