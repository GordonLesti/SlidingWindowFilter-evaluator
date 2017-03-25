package swf.calculator.distance;

import swf.calculator.Distance;

public class MultiplyDistance<T> implements Distance<Double, T> {
  private Distance<Double, T> dist1;
  private Distance<Double, T> dist2;

  public MultiplyDistance(Distance<Double, T> dist1, Distance<Double, T> dist2) {
    this.dist1 = dist1;
    this.dist2 = dist2;
  }

  public Double calculateDistance(T obj1, T obj2) {
    return this.dist1.calculateDistance(obj1, obj2) * this.dist2.calculateDistance(obj1, obj2);
  }
}
