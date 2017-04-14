package swf.accel.operator;

import swf.Accel;

public class Add implements swf.operator.Add<Accel> {
  /**
   * Adds to Accel data.
   */
  public Accel add(Accel accel1, Accel accel2) {
    return new Accel(
      accel1.getX() + accel2.getX(),
      accel1.getY() + accel2.getY(),
      accel1.getZ() + accel2.getZ()
    );
  }
}
