import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import org.junit.Test;
import swf.accel.io.TimeSeriesParser;
import swf.accel.model.AccelerationData;
import swf.model.TimeSeries;
import swf.model.timeseries.Item;

public class TimeSeriesParserTest {
  @Test
  public void testParseTimeSeriesFromFile() throws FileNotFoundException, IOException {
    LinkedList<Item<AccelerationData>> items = new LinkedList<Item<AccelerationData>>();
    items.add(new Item<AccelerationData>(0, new AccelerationData(0, 0, 0)));
    items.add(new Item<AccelerationData>(0, new AccelerationData(17, 40, 90)));
    items.add(new Item<AccelerationData>(119, new AccelerationData(-5, 10, 88)));
    items.add(new Item<AccelerationData>(8447, new AccelerationData(3, -44, 92)));
    items.add(new Item<AccelerationData>(11225, new AccelerationData(-10, -10, 84)));
    items.add(new Item<AccelerationData>(11225, new AccelerationData(-9, 12, 56)));
    items.add(new Item<AccelerationData>(12318, new AccelerationData(-42, -7, 90)));
    items.add(new Item<AccelerationData>(12320, new AccelerationData(-50, -28, 130)));
    items.add(new Item<AccelerationData>(15206, new AccelerationData(-35, -26, 80)));
    items.add(new Item<AccelerationData>(15206, new AccelerationData(-29, -16, 88)));
    LinkedList<Item<String>> flags = new LinkedList<Item<String>>();
    flags.add(new Item<String>(8425, "START 2"));
    flags.add(new Item<String>(10501, "END 2"));
    flags.add(new Item<String>(12318, "START 3"));
    flags.add(new Item<String>(14278, "END 3"));
    TimeSeries<AccelerationData> assertedTimeSeries =
        new TimeSeries<AccelerationData>(items, flags);
    TimeSeriesParser timeSeriesParser = new TimeSeriesParser();
    TimeSeries<AccelerationData> timeSeries =
        timeSeriesParser.parseTimeSeriesFromFile("build/resources/test/record1.txt");
    assertEquals(assertedTimeSeries, timeSeries);
  }
}
