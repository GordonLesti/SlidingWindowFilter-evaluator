package swf.io;

import swf.TimeSeries;

public interface TimeSeriesParser<T> {
  public TimeSeries<T> parseFile(String filename);
}
