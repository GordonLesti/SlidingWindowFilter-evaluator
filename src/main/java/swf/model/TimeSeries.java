package swf.model;

import java.util.List;
import swf.model.timeseries.Item;

public class TimeSeries<T> {

  private List<Item<T>> items;

  public TimeSeries(List<Item<T>> items) {
    this.items = items;
  }

  public List<Item<T>> getItems() {
    return this.items;
  }
}
