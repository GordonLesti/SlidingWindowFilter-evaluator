package swf.accel.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import swf.Accel;
import swf.TimeSeries;
import swf.timeseries.Point;

public class TimeSeriesParser implements swf.io.TimeSeriesParser<Accel> {
  /**
   * Parses a file and returns a TimeSeries of type Accel object.
   */
  public TimeSeries<Accel> parseFile(String filename) {
    try {
      BufferedReader br = new BufferedReader(new FileReader(filename));
      String line;
      LinkedList<String> lineList = new LinkedList<String>();
      while ((line = br.readLine()) != null) {
        lineList.add(line);
      }
      ListIterator<String> listIterator = lineList.listIterator();
      LinkedList<Accel> interval = new LinkedList<Accel>();
      TimeSeries<Accel> ts = new TimeSeries<Accel>();
      int intervalStart = 0;
      boolean record = false;
      String flag = "";
      while (listIterator.hasNext()) {
        line = listIterator.next();
        String[] split = line.split(" ");
        int time = Integer.parseInt(split[0]);
        if (split[1].equals("START")) {
          if (split[2].equals("17")) {
            record = false;
          }
          flag = split[2];
        } else if (split[1].equals("END")) {
          if (split[2].equals("0")) {
            record = true;
            intervalStart = time;
          }
          flag = "";
        } else if (record) {
          if (time < intervalStart + 50) {
            interval.add(
                new Accel(
                    Integer.parseInt(split[1]),
                    Integer.parseInt(split[2]),
                    Integer.parseInt(split[3])
                )
            );
          } else {
            if (interval.isEmpty()) {
              throw new IllegalArgumentException("Record is incomplete.");
            }
            ts.add(new Point<Accel>(this.meanAccel(interval), flag));
            interval = new LinkedList<Accel>();
            intervalStart += 30;
            boolean backInTime = true;
            while (listIterator.hasPrevious() && backInTime) {
              String[] prevSplit = listIterator.previous().split(" ");
              if (Integer.parseInt(prevSplit[0]) < intervalStart) {
                backInTime = false;
              }
            }
            listIterator.next();
          }
        }
      }
      if (!interval.isEmpty()) {
        ts.add(new Point<Accel>(this.meanAccel(interval), flag));
      }
      return ts;
    } catch (Exception exception) {
      return null;
    }
  }

  private Accel meanAccel(List<Accel> list) {
    int sumX = 0;
    int sumY = 0;
    int sumZ = 0;
    for (Accel accel : list) {
      sumX += accel.getX();
      sumY += accel.getY();
      sumZ += accel.getZ();
    }
    int size = list.size();
    return new Accel(
        this.quantize(sumX / size),
        this.quantize(sumY / size),
        this.quantize(sumZ / size)
    );
  }

  private int quantize(double axisDouble) {
    int axis = (int) Math.round(axisDouble);
    if (axis > 200) {
      return 16;
    }
    if (axis > 100) {
      return (axis - 100) / 20 + 11;
    }
    if (axis > 0) {
      return axis / 10 + 1;
    }
    if (axis == 0) {
      return 0;
    }
    if (axis >= -100) {
      return axis / 10 - 1;
    }
    if (axis >= -200) {
      return (axis + 100) / 20 - 11;
    }
    return -16;
  }
}
