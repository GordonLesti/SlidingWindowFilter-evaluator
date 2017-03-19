package swf.util;

import java.lang.Comparable;

public class ComparablePair<S, T extends Comparable<T>> implements
    Comparable<ComparablePair<S, T>> {
  private S content;
  private T measure;

  public ComparablePair(S content, T measure) {
    this.content = content;
    this.measure = measure;
  }

  public S getContent() {
    return this.content;
  }

  public int compareTo(ComparablePair<S, T> object) {
    return this.measure.compareTo(object.measure);
  }
}
