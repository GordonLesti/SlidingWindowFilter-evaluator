package swf;

import java.util.LinkedList;
import java.util.List;
import swf.timeseries.Point;

public class TimeSeries<T> extends LinkedList<Point<T>> {
  /**
   * Creates a new TimeSeries that contains only points with the given tag.
   */
  public TimeSeries<T> intervalByTag(String tag) {
    TimeSeries<T> ts = new TimeSeries<T>();
    for (Point<T> point : this) {
      if (point.getTag().equals(tag)) {
        ts.add(point);
      }
    }
    return ts;
  }

  /**
   * Creates a new TimeSeries that contains the points between fromIndex and toIndex.
   */
  public TimeSeries<T> subTimeSeries(int fromIndex, int toIndex) {
    List<Point<T>> subList = this.subList(fromIndex, toIndex);
    TimeSeries<T> ts = new TimeSeries<T>();
    for (Point<T> point : subList) {
      ts.add(point);
    }
    return ts;
  }
}
