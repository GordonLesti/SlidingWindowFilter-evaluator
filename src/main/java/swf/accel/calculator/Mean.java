package swf.accel.calculator;

import java.lang.IllegalArgumentException;
import java.util.Iterator;
import java.util.List;
import swf.accel.model.AccelerationData;

public class Mean implements swf.calculator.Mean<AccelerationData> {
  /**
   * Calculates the mean of a list of AccelerationData objects.
   */
  public AccelerationData calculateMean(List<AccelerationData> list) {
    if (list.size() == 0) {
      throw new IllegalArgumentException("Can not calculate mean of empty list");
    }
    Iterator<AccelerationData> iterator = list.iterator();
    int sumX = 0;
    int sumY = 0;
    int sumZ = 0;
    while (iterator.hasNext()) {
      AccelerationData accelData = iterator.next();
      sumX += accelData.getX();
      sumY += accelData.getY();
      sumZ += accelData.getZ();
    }
    int size = list.size();
    return new AccelerationData(
        (int)Math.round((sumX * 1.0) / size),
        (int)Math.round((sumY * 1.0) / size),
        (int)Math.round((sumZ * 1.0) / size)
    );
  }
}
