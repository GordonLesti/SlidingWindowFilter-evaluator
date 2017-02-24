package swf.accel.transformer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import swf.accel.model.AccelerationData;
import swf.model.TimeSeries;
import swf.model.timeseries.Item;
import swf.transformer.TimeSeriesTransformer;

public class QuantizeTransformer implements TimeSeriesTransformer<AccelerationData> {
  /**
   * Quantizes every AccelerationData in TimeSeries.
   */
  public TimeSeries<AccelerationData> transform(TimeSeries<AccelerationData> timeSeries) {
    List<Item<AccelerationData>> accelList = timeSeries.getItems();
    LinkedList<Item<AccelerationData>> quantizedList = new LinkedList<Item<AccelerationData>>();
    Iterator<Item<AccelerationData>> iterator = accelList.iterator();
    while (iterator.hasNext()) {
      Item<AccelerationData> item = iterator.next();
      AccelerationData accelData = item.getData();
      quantizedList.add(
          new Item<AccelerationData>(
              item.getTime(),
              new AccelerationData(
                  this.transformAccelValue(accelData.getX()),
                  this.transformAccelValue(accelData.getY()),
                  this.transformAccelValue(accelData.getZ())
              )
          )
      );
    }
    return new TimeSeries<AccelerationData>(quantizedList, timeSeries.getFlags());
  }

  private static int transformAccelValue(int accelValue) {
    if (accelValue > 200) {
      return 16;
    }
    if (accelValue > 100) {
      return ((accelValue - 100) / 20) + 11;
    }
    if (accelValue > 0) {
      return (accelValue / 10) + 1;
    }
    if (accelValue == 0) {
      return 0;
    }
    if (accelValue >= -100) {
      return (accelValue / 10) - 1;
    }
    if (accelValue >= -200) {
      return ((accelValue + 100) / 20) - 11;
    }
    return -16;
  }
}
