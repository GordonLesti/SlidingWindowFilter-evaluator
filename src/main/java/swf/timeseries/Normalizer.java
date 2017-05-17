package swf.timeseries;

import swf.TimeSeries;

public interface Normalizer<T> {
  public TimeSeries<T> normalize(TimeSeries<T> ts);
}
