import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import org.junit.Test;
import swf.model.TimeSeries;
import swf.model.timeseries.Item;
import swf.transformer.ResetTimeTransformer;

public class ResetTimeTransformerTest {
  @Test
  public void testTransform() {
    LinkedList<Item<Integer>> itemList = new LinkedList<Item<Integer>>();
    itemList.add(new Item<Integer>(12, 14));
    itemList.add(new Item<Integer>(77, 6));
    LinkedList<Item<String>> flagList = new LinkedList<Item<String>>();
    flagList.add(new Item<String>(20, "eCHFjjnD"));
    flagList.add(new Item<String>(51, "DzKFvtt8"));
    LinkedList<Item<Integer>> assertedItemList = new LinkedList<Item<Integer>>();
    assertedItemList.add(new Item<Integer>(0, 14));
    assertedItemList.add(new Item<Integer>(65, 6));
    LinkedList<Item<String>> assertedFlagList = new LinkedList<Item<String>>();
    assertedFlagList.add(new Item<String>(8, "eCHFjjnD"));
    assertedFlagList.add(new Item<String>(39, "DzKFvtt8"));
    ResetTimeTransformer<Integer> resetTimeTransformer =
        new ResetTimeTransformer<Integer>();
    assertEquals(
        new TimeSeries<Integer>(assertedItemList, assertedFlagList),
        resetTimeTransformer.transform(new TimeSeries<Integer>(itemList, flagList))
    );
  }
}
