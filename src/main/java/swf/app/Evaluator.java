package swf.app;

import java.util.List;
import swf.accel.model.AccelerationData;
import swf.model.TimeSeries;

public interface Evaluator {
  public String evaluate(List<TimeSeries<AccelerationData>> timeSeriesList);
}
