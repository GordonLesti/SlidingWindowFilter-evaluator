import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import swf.calculator.Subtraction;
import swf.calculator.measure.Mean;
import swf.model.TimeSeries;
import swf.model.timeseries.Item;
import swf.transformer.NormalizeTransformer;

public class NormalizeTransformerTest {
  @Test
  public void testTransform() {
    LinkedList<Item<Double>> itemList = new LinkedList<Item<Double>>();
    itemList.add(new Item<Double>(4, 849.68));
    itemList.add(new Item<Double>(51, -701.62));
    itemList.add(new Item<Double>(80, -409.98));
    LinkedList<Item<String>> flagList = new LinkedList<Item<String>>();
    flagList.add(new Item<String>(50, "YVtttkrMmS7UpIb2"));
    LinkedList<Item<Double>> assertedItemList = new LinkedList<Item<Double>>();
    double assertedMean = -261.92 / 3;
    assertedItemList.add(new Item<Double>(4, 849.68 - assertedMean));
    assertedItemList.add(new Item<Double>(51, -701.62 - assertedMean));
    assertedItemList.add(new Item<Double>(80, -409.98 - assertedMean));
    TimeSeries<Double> timeSeries = new TimeSeries<Double>(itemList, flagList);
    NormalizeTransformer<Double> normalizeTransformer = this.createNormalizeTransformer();
    assertEquals(
        new TimeSeries<Double>(assertedItemList, flagList),
        normalizeTransformer.transform(timeSeries)
    );
  }

  private NormalizeTransformer<Double> createNormalizeTransformer() {
    return new NormalizeTransformer<Double>(
        new Subtraction<Double>() {
          public Double subtract(Double minuend, Double subtrahend) {
            return minuend - subtrahend;
          }
        },
        new Mean<Double>() {
          public Double calculate(List<Double> doubleList) {
            Double mean = 0.0;
            Iterator<Double> iterator = doubleList.iterator();
            while (iterator.hasNext()) {
              mean += iterator.next();
            }
            return mean / doubleList.size();
          }
        }
    );
  }
}
