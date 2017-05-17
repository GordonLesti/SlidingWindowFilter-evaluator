package swf.measure.timeseries;

import swf.TimeSeries;
import swf.measure.Distance;
import swf.timeseries.Normalizer;

public class NormalizedDistance<T> implements Distance<TimeSeries<T>> {
  private Distance<TimeSeries<T>> distance;
  private Normalizer<T> normalizer;

  /**
   * Creates a NormalizedDistance that normalizes time series before calculating distance.
   */
  public NormalizedDistance(Distance<TimeSeries<T>> distance, Normalizer<T> normalizer) {
    this.distance = distance;
    this.normalizer = normalizer;
  }

  /**
   * Normalizes time series and calculate the distance afterwards.
   */
  public double distance(TimeSeries<T> ts1, TimeSeries<T> ts2) {
    return this.distance.distance(
        this.normalizer.normalize(ts1),
        this.normalizer.normalize(ts2)
    );
  }
}
