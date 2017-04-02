package swf.calculator.filter;

import swf.calculator.Filter;
import swf.model.TimeSeries;

public class TrueTimeSeriesFilter<T> implements Filter<TimeSeries<T>> {
  public boolean filter(TimeSeries<T> timeSeries) {
    return true;
  }
}
