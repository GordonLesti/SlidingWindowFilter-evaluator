package swf.measure;

public class MultiplyDistance<T> implements Distance<T> {
  private Distance<T> distance1;
  private Distance<T> distance2;

  public MultiplyDistance(Distance<T> distance1, Distance<T> distance2) {
    this.distance1 = distance1;
    this.distance2 = distance2;
  }

  public double distance(T obj1, T obj2) {
    return this.distance1.distance(obj1, obj2) * this.distance2.distance(obj1, obj2);
  }
}
