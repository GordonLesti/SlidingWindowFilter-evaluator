package swf.measure.timeseries.dynamictimewarping;

public interface AdjustmentWindowCondition {
  public boolean isConditionFulfilled(int indexI, int indexJ, int lengthI, int lengthJ);
}
