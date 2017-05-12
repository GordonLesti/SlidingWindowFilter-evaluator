package swf.measure.timeseries.dynamictimewarping;

public class SakoeChibaBand implements AdjustmentWindowCondition {
  private double conditionFactor;

  public SakoeChibaBand(double conditionFactor) {
    this.conditionFactor = conditionFactor;
  }

  /**
   * Calculates if warping path is allowed at point i and j.
   */
  public boolean isConditionFulfilled(int indexI, int indexJ, int lengthI, int lengthJ) {
    int middleJ = (int) Math.ceil((1.0 * lengthJ * indexI) / lengthI);
    int window = Math.max(
        (int) Math.round(lengthJ * this.conditionFactor),
        Math.abs(lengthI - lengthJ)
    );
    return indexJ >= Math.max(0, middleJ - window)
        && indexJ < Math.min(lengthJ, middleJ + window + lengthJ % 2);
  }
}
