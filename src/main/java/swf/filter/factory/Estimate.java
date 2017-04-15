package swf.filter.factory;

import java.util.Collection;
import swf.filter.Factory;
import swf.filter.Filter;

public class Estimate<T> implements Factory<T> {
  private swf.measure.Estimate<T> estimate;
  private double blurFactor;

  public Estimate(swf.measure.Estimate<T> estimate, double blurFactor) {
    this.estimate = estimate;
    this.blurFactor = blurFactor;
  }

  public Filter<T> create(Collection<T> collection) {
    return new swf.filter.Estimate<T>(this.estimate, collection, blurFactor);
  }
}
