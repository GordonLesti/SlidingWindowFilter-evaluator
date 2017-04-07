package swf.calculator.distance;

import swf.calculator.Distance;
import swf.model.TimeSeries;
import swf.transformer.NormalizeTransformer;

public class NormalizeDynamicTimeWarping<U> implements Distance<Double, TimeSeries<U>> {
  private DynamicTimeWarping<U> dtw;
  private NormalizeTransformer<U> normalizeTransformer;

  public NormalizeDynamicTimeWarping(
      DynamicTimeWarping<U> dtw,
      NormalizeTransformer<U> normalizeTransformer
  ) {
    this.dtw = dtw;
    this.normalizeTransformer = normalizeTransformer;
  }

  /**
   * Normalizes both TimeSeries before calculating Dynamic Time Warping.
   */
  public Double calculateDistance(TimeSeries<U> timeSeries1, TimeSeries<U> timeSeries2) {
    return this.dtw.calculateDistance(
        this.normalizeTransformer.transform(timeSeries1),
        this.normalizeTransformer.transform(timeSeries2)
    );
  }
}
