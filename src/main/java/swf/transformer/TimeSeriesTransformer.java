package swf.transformer;

import swf.model.TimeSeries;

public interface TimeSeriesTransformer<T> {
  public TimeSeries<T> transform(TimeSeries<T> timeSeries);
}
