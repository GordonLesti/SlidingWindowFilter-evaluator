package swf;

import java.io.FileNotFoundException;
import java.io.IOException;
import swf.accel.io.TimeSeriesParser;
import swf.accel.model.AccelerationData;
import swf.model.TimeSeries;

public class App {
  /**
   * Starts the App.
   */
  public static void main(String[] args) {
    TimeSeriesParser timeSeriesParser = new TimeSeriesParser();
    String filename = "build/resources/main/record1.txt";
    try {
      TimeSeries<AccelerationData> timeSeries =
          timeSeriesParser.parseTimeSeriesFromFile(filename);
      System.out.println("YOYO");
    } catch (FileNotFoundException fnfe) {
      System.out.println("Can not find file " + filename);
    } catch (IOException ioe) {
      System.out.println("Problems while reading file " + filename);
    }
  }
}
