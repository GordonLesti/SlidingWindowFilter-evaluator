package swf.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import swf.model.AccelerationData;
import swf.model.TimeSeries;
import swf.model.timeseries.Item;

public class AccelTimeSeries {
  /**
   * Creates a TimeSeries of AccelerationData object from a given filename.
   */
  public static TimeSeries<AccelerationData> createAccelTimeSeriesFromFile(String filename)
      throws FileNotFoundException, IOException {
    BufferedReader br = new BufferedReader(new FileReader(filename));
    String line;
    LinkedList<Item<AccelerationData>> items = new LinkedList<Item<AccelerationData>>();
    LinkedList<Item<String>> flags = new LinkedList<Item<String>>();
    while ((line = br.readLine()) != null) {
      String[] split = line.split(" ");
      long time = Long.parseLong(split[0]);
      if (split[1].equals("START") || split[1].equals("END")) {
        flags.add(new Item<String>(time, split[1] + " " + split[2]));
      } else {
        AccelerationData accel = new AccelerationData(
            Integer.parseInt(split[1]),
            Integer.parseInt(split[2]),
            Integer.parseInt(split[3])
        );
        items.add(new Item<AccelerationData>(time, accel));
      }
    }
    return new TimeSeries<AccelerationData>(items, flags);
  }
}
