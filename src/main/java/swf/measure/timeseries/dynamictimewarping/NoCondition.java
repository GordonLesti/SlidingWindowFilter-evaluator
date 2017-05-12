package swf.measure.timeseries.dynamictimewarping;

public class NoCondition implements AdjustmentWindowCondition {
  public boolean isConditionFulfilled(int indexI, int indexJ, int lengthI, int lengthJ) {
    return true;
  }
}
