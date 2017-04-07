import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import org.junit.Test;
import swf.accel.io.TimeSeriesParser;
import swf.accel.model.AccelerationData;
import swf.model.TimeSeries;
import swf.model.timeseries.Item;
import swf.transformer.SubFlagTransformer;

public class SubFlagTransformerTest {
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
    SubFlagTransformer<AccelerationData> subTransformer =
        new SubFlagTransformer<AccelerationData>("END 2", "START 3");
    assertEquals(
        new TimeSeries<AccelerationData>(assertedAccelList, assertedFlagsList),
        subTransformer.transform(timeSeries)
    );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTransformDuplicateStartFlag() {
    LinkedList<Item<String>> flags = new LinkedList<Item<String>>();
    flags.add(new Item<String>(36, "rLjCso9rJ1getwrF"));
    flags.add(new Item<String>(53, "Rg45yJC5rBNNowEc"));
    flags.add(new Item<String>(79, "rLjCso9rJ1getwrF"));
    SubFlagTransformer<String> subTransformer =
        new SubFlagTransformer<String>("rLjCso9rJ1getwrF", "Rg45yJC5rBNNowEc");
    TimeSeries<String> timeSeries = new TimeSeries<String>(
        new LinkedList<Item<String>>(),
        flags
    );
    TimeSeries<String> subTimeSeries = subTransformer.transform(timeSeries);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTransformDuplicateEndFlag() {
    LinkedList<Item<String>> flags = new LinkedList<Item<String>>();
    flags.add(new Item<String>(36, "wiY0VbOydacAnKWu"));
    flags.add(new Item<String>(53, "fHccuBuAepfkDmNe"));
    flags.add(new Item<String>(79, "fHccuBuAepfkDmNe"));
    SubFlagTransformer<String> subTransformer =
        new SubFlagTransformer<String>("wiY0VbOydacAnKWu", "fHccuBuAepfkDmNe");
    TimeSeries<String> timeSeries = new TimeSeries<String>(
        new LinkedList<Item<String>>(),
        flags
    );
    TimeSeries<String> subTimeSeries = subTransformer.transform(timeSeries);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTransformDuplicateNoFlag() {
    LinkedList<Item<String>> flags = new LinkedList<Item<String>>();
    flags.add(new Item<String>(36, "wiY0VbOydacAnKWu"));
    flags.add(new Item<String>(53, "fHccuBuAepfkDmNe"));
    SubFlagTransformer<String> subTransformer =
        new SubFlagTransformer<String>("wiY0VbOydacAnKWu", "7fEoXOCeI8qOiLix");
    TimeSeries<String> timeSeries = new TimeSeries<String>(
        new LinkedList<Item<String>>(),
        flags
    );
    TimeSeries<String> subTimeSeries = subTransformer.transform(timeSeries);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTransformDuplicateWrongFlagOrder() {
    LinkedList<Item<String>> flags = new LinkedList<Item<String>>();
    flags.add(new Item<String>(36, "wiY0VbOydacAnKWu"));
    flags.add(new Item<String>(53, "fHccuBuAepfkDmNe"));
    SubFlagTransformer<String> subTransformer =
        new SubFlagTransformer<String>("fHccuBuAepfkDmNe", "wiY0VbOydacAnKWu");
    TimeSeries<String> timeSeries = new TimeSeries<String>(
        new LinkedList<Item<String>>(),
        flags
    );
    TimeSeries<String> subTimeSeries = subTransformer.transform(timeSeries);
  }
}
