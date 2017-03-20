import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import org.junit.Test;
import swf.accel.calculator.Mean;
import swf.accel.io.TimeSeriesParser;
import swf.accel.model.AccelerationData;
import swf.accel.transformer.QuantizeTransformer;
import swf.model.TimeSeries;
import swf.model.timeseries.Item;
import swf.transformer.ChainTransformer;
import swf.transformer.MeanTransformer;
import swf.transformer.SubTransformer;
import swf.transformer.TimeSeriesTransformer;

public class ChainTransformerTest {
  @Test
  public void testTransform() throws FileNotFoundException, IOException {
    LinkedList<TimeSeriesTransformer<AccelerationData>> transformerList =
        new LinkedList<TimeSeriesTransformer<AccelerationData>>();
    transformerList.add(new SubTransformer<AccelerationData>("START 3", "END 3"));
    transformerList.add(new MeanTransformer<AccelerationData>(50, 30, new Mean()));
    transformerList.add(new QuantizeTransformer());
    LinkedList<Item<AccelerationData>> accelList = new LinkedList<Item<AccelerationData>>();
    accelList.add(new Item<AccelerationData>(12318, new AccelerationData(-5, -2, 11)));
    LinkedList<Item<String>> flagList = new LinkedList<Item<String>>();
    flagList.add(new Item<String>(12318, "START 3"));
    flagList.add(new Item<String>(14278, "END 3"));
    TimeSeriesParser timeSeriesParser = new TimeSeriesParser();
    TimeSeries<AccelerationData> timeSeries =
        timeSeriesParser.parseTimeSeriesFromFile("build/resources/test/record1.txt");
    ChainTransformer<AccelerationData> chainTransformer =
        new ChainTransformer<AccelerationData>(transformerList);
    assertEquals(
        new TimeSeries<AccelerationData>(accelList, flagList),
        chainTransformer.transform(timeSeries)
    );
  }
}
