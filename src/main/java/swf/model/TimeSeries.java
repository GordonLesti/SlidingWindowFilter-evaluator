package swf.model;

import java.util.List;
import swf.model.timeseries.Item;

public class TimeSeries<T> {

  private List<Item<T>> items;

  private List<Item<String>> flags;

  public TimeSeries(List<Item<T>> items, List<Item<String>> flags) {
    this.items = items;
    this.flags = flags;
  }

  public List<Item<T>> getItems() {
    return this.items;
  }

  public List<Item<String>> getFlags() {
    return this.flags;
  }
}
