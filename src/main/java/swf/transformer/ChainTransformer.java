package swf.transformer;

import java.util.Iterator;
import java.util.List;
import swf.model.TimeSeries;

public class ChainTransformer<T> implements TimeSeriesTransformer<T> {
  private List<TimeSeriesTransformer<T>> transformerList;
  public ChainTransformer(List<TimeSeriesTransformer<T>> transformerList) {
    this.transformerList = transformerList;
  }

  public TimeSeries<T> transform(TimeSeries<T> timeSeries) {
    TimeSeries<T> transformedTimeSeries = timeSeries;
    Iterator<TimeSeriesTransformer<T>> iterator = this.transformerList.iterator();
    while (iterator.hasNext()) {
      TimeSeriesTransformer<T> transformer = iterator.next();
      transformedTimeSeries = transformer.transform(transformedTimeSeries);
    }
    return transformedTimeSeries;
  }
}
