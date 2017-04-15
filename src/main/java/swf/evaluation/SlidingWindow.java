package swf.evaluation;

import java.lang.Comparable;
import java.util.LinkedList;
import java.util.List;
import swf.Accel;
import swf.TimeSeries;
import swf.evaluation.slidingwindow.Threshold;
import swf.evaluation.slidingwindow.WindowSize;
import swf.measure.Distance;
import swf.nnc.Factory;

public class SlidingWindow implements Comparable<SlidingWindow> {
  private List<TimeSeries<Accel>> tsList;
  private Distance<TimeSeries<Accel>> distance;
  private Factory<TimeSeries<Accel>> nncFactory;
  private WindowSize windowSize;
  private Threshold threshold;
  private String name;
  private List<TimeSeries<Accel>> resultTsList;

  /**
   * Creates an evaluator for sliding window filters on TimeSeries of Accel data.
   */
  public SlidingWindow(
      List<TimeSeries<Accel>> tsList,
      Distance<TimeSeries<Accel>> distance,
      Factory<TimeSeries<Accel>> nncFactory,
      WindowSize windowSize,
      Threshold threshold,
      String name
  ) {
    this.tsList = tsList;
    this.distance = distance;
    this.nncFactory = nncFactory;
    this.windowSize = windowSize;
    this.threshold = threshold;
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public int compareTo(SlidingWindow swf) {
    return 0;
  }

  private void slideOverTimeSeries(TimeSeries<Accel> ts) {
    LinkedList<TimeSeries<Accel>> testData = new LinkedList<TimeSeries<Accel>>();
    for (int i = 1; i < 9; i++) {
      testData.add(ts.intervalByTag(Integer.toString(i)));
    }
    int fromIndex = ts.lastIndexOf(ts.intervalByTag(Integer.toString(8)).getLast()) + 1;
    int toIndex = ts.size();
    TimeSeries<Accel> record = ts.subTimeSeries(fromIndex, toIndex);
    int windowSize = this.windowSize.windowSize(testData);
    int stepSize = (int) (windowSize / 10.0);
  }
}
