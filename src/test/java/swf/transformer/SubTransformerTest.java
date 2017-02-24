import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import org.junit.Test;
import swf.accel.io.TimeSeriesParser;
import swf.accel.model.AccelerationData;
import swf.model.TimeSeries;
import swf.model.timeseries.Item;
import swf.transformer.SubTransformer;

public class SubTransformerTest {
  @Test
  public void testTransform() throws FileNotFoundException, IOException {
    LinkedList<Item<AccelerationData>> assertedAccelList = new LinkedList<Item<AccelerationData>>();
    assertedAccelList.add(new Item<AccelerationData>(11225, new AccelerationData(-10, -10, 84)));
    assertedAccelList.add(new Item<AccelerationData>(11225, new AccelerationData(-9, 12, 56)));
    assertedAccelList.add(new Item<AccelerationData>(12318, new AccelerationData(-42, -7, 90)));
    LinkedList<Item<String>> assertedFlagsList = new LinkedList<Item<String>>();
    assertedFlagsList.add(new Item<String>(10501, "END 2"));
    assertedFlagsList.add(new Item<String>(12318, "START 3"));
    TimeSeriesParser timeSeriesParser = new TimeSeriesParser();
    TimeSeries<AccelerationData> timeSeries =
        timeSeriesParser.parseTimeSeriesFromFile("build/resources/test/record1.txt");
    SubTransformer<AccelerationData> subTransformer =
        new SubTransformer<AccelerationData>("END 2", "START 3");
    assertEquals(
        new TimeSeries<AccelerationData>(assertedAccelList, assertedFlagsList),
        subTransformer.transform(timeSeries)
    );
  }
}
