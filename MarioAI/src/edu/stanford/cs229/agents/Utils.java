package edu.stanford.cs229.agents;

public class Utils {

  public static boolean getBit(int number, int i) {
    return (number & (1 << i)) != 0;
  }
  
  public static String printBits(int number, int n) {
    String s = "";
    for (int i = 0; i < n; i++) {
      s += getBit(number, i) ? "1" : "0";
    }
    return s;
  }
  
  public static String printArray(boolean[] array) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < array.length; i++) {
      if (i > 0) {
        sb.append(" ");
      }
      sb.append(array[i] ? "1" : "0");
    }
    return sb.reverse().toString();
  }
  
  public static String join(Object[] items, String separator) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < items.length; i++) {
      if (i > 0) {
        sb.append(separator);
      }
      sb.append(items[i].toString());
    }
    return sb.toString();
  }
  
  public static String join(float[] items, String separator) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < items.length; i++) {
      if (i > 0) {
        sb.append(separator);
      }
      sb.append(String.format("%.6f", items[i]));
    }
    return sb.toString();
  }
  
  public static String join(int[] items, String separator) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < items.length; i++) {
      if (i > 0) {
        sb.append(separator);
      }
      sb.append(String.format("%d", items[i]));
    }
    return sb.toString();
  }
}
