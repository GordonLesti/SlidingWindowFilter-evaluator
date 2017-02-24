package swf.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import swf.model.TimeSeries;

public interface TimeSeriesParser<T> {
  public TimeSeries<T> parseTimeSeriesFromFile(String filename)
      throws FileNotFoundException, IOException;
}
