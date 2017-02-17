package swf.model;

import java.util.List;
import java.util.Objects;
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

  /**
   * Checks if the given object equals the TimeSeries.
   */
  public boolean equals(Object object) {
    if (object instanceof TimeSeries) {
      TimeSeries timeSeries = (TimeSeries) object;
      return this.getItems().equals(timeSeries.getItems())
          && this.getFlags().equals(timeSeries.getFlags());
    }
    return false;
  }

  public int hashCode() {
    return Objects.hash(this.getItems(), this.getFlags());
  }
}
