package swf.calculator.measure;

import swf.calculator.Measure;
import swf.model.TimeSeries;

public class AverageComplexity<T> implements Measure<Double, TimeSeries<T>> {
  private Complexity<T> complexityCalculator;

  public AverageComplexity(Complexity<T> complexityCalculator) {
    this.complexityCalculator = complexityCalculator;
  }

  public Double calculate(TimeSeries<T> timeSeries) {
    return this.complexityCalculator.calculate(timeSeries) / timeSeries.getItems().size();
  }
}
