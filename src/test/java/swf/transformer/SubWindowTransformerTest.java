import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import org.junit.Test;
import swf.accel.io.TimeSeriesParser;
import swf.accel.model.AccelerationData;
import swf.model.TimeSeries;
import swf.model.timeseries.Item;
import swf.transformer.SubWindowTransformer;

public class SubWindowTransformerTest {
  @Test
  public void testTransform() throws FileNotFoundException, IOException {
    LinkedList<Item<AccelerationData>> assertedAccelList = new LinkedList<Item<AccelerationData>>();
    assertedAccelList.add(new Item<AccelerationData>(1310, new AccelerationData(35, 8, 28)));
    assertedAccelList.add(new Item<AccelerationData>(1315, new AccelerationData(43, 7, 8)));
    assertedAccelList.add(new Item<AccelerationData>(1320, new AccelerationData(48, 24, 34)));
    assertedAccelList.add(new Item<AccelerationData>(1325, new AccelerationData(47, 30, 35)));
    LinkedList<Item<String>> assertedFlagsList = new LinkedList<Item<String>>();
    assertedFlagsList.add(new Item<String>(1320, "START 0"));
    TimeSeriesParser timeSeriesParser = new TimeSeriesParser();
    TimeSeries<AccelerationData> timeSeries =
        timeSeriesParser.parseTimeSeriesFromFile("build/resources/test/record2.txt");
    SubWindowTransformer<AccelerationData> subWindowTransformer =
        new SubWindowTransformer<AccelerationData>(1310, 1328);
    assertEquals(
        new TimeSeries<AccelerationData>(assertedAccelList, assertedFlagsList),
        subWindowTransformer.transform(timeSeries)
    );
  }
}
