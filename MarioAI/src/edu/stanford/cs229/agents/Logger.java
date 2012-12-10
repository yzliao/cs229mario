package edu.stanford.cs229.agents;

public class Logger {

  public static void println(int level, String message) {
    Logger.print(level, message + "\n");
  }
  
  public static void println(int level, Object message) {
    Logger.println(level, message.toString());
  }
  
  public static void println(int level, int number) {
    Logger.println(level, String.valueOf(number));
  }
  
  public static void println(int level, String format, Object... values) {
    Logger.println(level, String.format(format, values));
  }
  
  public static void print(int level, String message) {
    if (level <= LearningParams.DEBUG) {
      System.out.print(message);
    }
  }
}
