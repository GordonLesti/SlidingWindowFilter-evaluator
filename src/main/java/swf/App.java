package swf;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import swf.accel.calculator.Distance;
import swf.accel.calculator.Mean;
import swf.accel.io.TimeSeriesParser;
import swf.accel.model.AccelerationData;
import swf.accel.transformer.QuantizeTransformer;
import swf.calculator.distance.DynamicTimeWarping;
import swf.calculator.measure.Complexity;
import swf.model.TimeSeries;
import swf.transformer.MeanTransformer;
import swf.transformer.SubTransformer;

public class App {
  /**
   * Starts the App.
   */
  public static void main(String[] args) {
    TimeSeriesParser timeSeriesParser = new TimeSeriesParser();
    QuantizeTransformer quantizeTransformer = new QuantizeTransformer();
    MeanTransformer<AccelerationData> meanTransformer =
        new MeanTransformer<AccelerationData>(50, 30, new Mean());
    SubTransformer<AccelerationData> subTrans =
        new SubTransformer<AccelerationData>("END 0", "START 17");
    String filename = "build/resources/main/record1.txt";
    try {
      TimeSeries<AccelerationData> timeSeries =
          quantizeTransformer.transform(
              meanTransformer.transform(
                  subTrans.transform(
                      timeSeriesParser.parseTimeSeriesFromFile(filename)
                  )
              )
          );
      LinkedList<TimeSeries<AccelerationData>> library =
          new LinkedList<TimeSeries<AccelerationData>>();
      LinkedList<TimeSeries<AccelerationData>> gestures =
          new LinkedList<TimeSeries<AccelerationData>>();
      for (int i = 1; i < 9; i++) {
        SubTransformer<AccelerationData> subTransformer =
            new SubTransformer<AccelerationData>("START " + i, "END " + i);
        library.add(subTransformer.transform(timeSeries));
        subTransformer =
            new SubTransformer<AccelerationData>("START " + (i + 8), "END " + (i + 8));
        gestures.add(subTransformer.transform(timeSeries));
      }
      Iterator<TimeSeries<AccelerationData>> iterator1 = library.iterator();
      DynamicTimeWarping<AccelerationData> dtw =
          new DynamicTimeWarping<AccelerationData>(new Distance());
      String format = "| Libgesture %d | %9.3f | %9.3f | %9.3f | %9.3f | %9.3f | %9.3f | %9.3f | %9.3f |%n";
      int i = 0;
      System.out.println(" _                ___                            \n| \\  ._  _.._ _ o _|o._ _  _\\    /_.._._ o._  _  \n|_/\\/| |(_|| | ||(_||| | |(/_\\/\\/(_|| |_)|| |(_| \n   /                                  |       _|\n");
      System.out.println("+--------------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+");
      System.out.println("|              | Gesture 1 | Gesture 2 | Gesture 3 | Gesture 4 | Gesture 5 | Gesture 6 | Gesture 7 | Gesture 8 |");
      System.out.println("+--------------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+");
      while (iterator1.hasNext()) {
        TimeSeries<AccelerationData> timeSeries1 = iterator1.next();
        Iterator<TimeSeries<AccelerationData>> iterator2 = gestures.iterator();
        int j = 0;
        double[] result = new double[8];
        while (iterator2.hasNext()) {
          TimeSeries<AccelerationData> timeSeries2 = iterator2.next();
          result[j] = dtw.calculateDistance(timeSeries1, timeSeries2);
          j++;
        }
        i++;
        System.out.format(
            format,
            i,
            result[0],
            result[1],
            result[2],
            result[3],
            result[4],
            result[5],
            result[6],
            result[7]
        );
      }
      System.out.println("+--------------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+\n");
      System.out.println(" _                        \n/  _ ._ _ ._ | _   o_|_   \n\\_(_)| | ||_)|(/_><| |_\\/ \n          |            /  \n");
      Complexity<AccelerationData> complexity = new Complexity<AccelerationData>(new Distance());
      iterator1 = library.iterator();
      i = 0;
      System.out.println("+--------------+-----------+----+");
      System.out.println("|         Name |   Complex |  # |");
      System.out.println("+--------------+-----------+----+");
      while (iterator1.hasNext()) {
        TimeSeries<AccelerationData> timeSeries1 = iterator1.next();
        System.out.format("| Libgesture %d | %9.3f | %d |%n", i + 1, complexity.calculate(timeSeries1), timeSeries1.getItems().size());
        i++;
      }
      Iterator<TimeSeries<AccelerationData>> iterator2 = gestures.iterator();
      i = 0;
      while (iterator2.hasNext()) {
        TimeSeries<AccelerationData> timeSeries1 = iterator2.next();
        System.out.format("|    Gesture %d | %9.3f | %d |%n", i + 1, complexity.calculate(timeSeries1), timeSeries1.getItems().size());
        i++;
      }
      System.out.println("+--------------+-----------+----+");
    } catch (FileNotFoundException fnfe) {
      System.out.println("Can not find file " + filename);
    } catch (IOException ioe) {
      System.out.println("Problems while reading file " + filename);
    }
  }
}
