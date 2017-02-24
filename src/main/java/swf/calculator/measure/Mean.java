package swf.calculator.measure;

import java.util.List;
import swf.calculator.Measure;

public interface Mean<U> extends Measure<U, List<U>> {
  public U calculate(List<U> list);
}
