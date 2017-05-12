package swf.filter;

import java.util.Collection;
import swf.filter.Filter;

public class Estimate<T> implements Filter<T> {
  private swf.measure.Estimate<T> estimate;
  private double lowerBound;
  private double upperBound;

  /**
   * Creates a filter baed on an estimate.
   */
  public Estimate(
      swf.measure.Estimate<T> estimate,
      Collection<T> tsCollection,
      double blurFactor
  ) {
    this.estimate = estimate;
    double min = Double.POSITIVE_INFINITY;
    double max = Double.NEGATIVE_INFINITY;
    for (T obj : tsCollection) {
      double estValue = this.estimate.estimate(obj);
      if (estValue < min) {
        min = estValue;
      }
      if (estValue > max) {
        max = estValue;
      }
    }
    double diff = Math.abs(max - min);
    this.upperBound = min + diff * blurFactor;
    this.lowerBound = max - diff * blurFactor;
  }

  /**
   * Filters is a object if in interval of estimate.
   */
  public boolean filter(T obj) {
    double estValue = this.estimate.estimate(obj);
    if (estValue >= this.lowerBound && estValue <= this.upperBound) {
      return true;
    }
    return false;
  }
}
