package swf;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class App {
  /**
   * Starts the App.
   */
  public static void main(String[] args) {
    String recordsDir = "build/resources/main/";
    List<String> recordFilenames = getRecordFilenames(recordsDir);
    Iterator<String> recordFilenameIterator = recordFilenames.iterator();
    while (recordFilenameIterator.hasNext()) {
      System.out.println(recordFilenameIterator.next());
    }
  }

  /**
   * Returns a list of filenames from the given directory.
   */
  public static List<String> getRecordFilenames(String dirpath) {
    File dir = new File(dirpath);
    LinkedList<String> files = new LinkedList<String>();
    for (File file : dir.listFiles()) {
      if (file.isFile()) {
        files.add(file.getName());
      }
    }
    return files;
  }
}
