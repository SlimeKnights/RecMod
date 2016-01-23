package fuj1n.recmod.legacy;

import fuj1n.recmod.RecMod;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * A reader of the raw config which reads all the values and calls a write in RecMod to an FML config.
 * Overhead of this is to be removed in 1.8
 *
 * @author fuj1n
 */
public class OldConfigConverter {

  public static File configFile;

  public static void convert() {
    RecMod rm = RecMod.instance;

    if (!configFile.exists()) {
      return;
    }

    try {
      BufferedReader b = new BufferedReader(new FileReader(configFile));
      String line1 = b.readLine();
      String line2 = b.readLine();
      String line3 = b.readLine();
      String line4 = b.readLine();
      String line5 = b.readLine();
      String line6 = b.readLine();

      //Discarded line
      //			rm.sheetLocation = line2 != null && !line2.equals("") ? line2 : rm.sheetLocation;
      rm.enableKeys = convertToBoolean(line3, false);
      rm.keyRec = convertToInteger(line4, 44);
      rm.keyStr = convertToInteger(line5, 45);
      rm.keepState = convertToBoolean(line6, false);

      b.close();

      rm.instanciateConfig();
      rm.writeToFile();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      //Don't want the massive stacktrace if the file read fails.
      //e.printStackTrace();
    }
  }

  private static int convertToInteger(String s, int def) {
    try {
      return Integer.parseInt(s);
    } catch (Exception e) {
    }
    return def;
  }

  private static boolean convertToBoolean(String s, boolean def) {
    try {
      return Boolean.parseBoolean(s);
    } catch (Exception e) {
    }
    return def;
  }

}
