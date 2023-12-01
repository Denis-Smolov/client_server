package denissmolov.utils;

public class Logger {

  public static void log(String message) {
    System.out.println(message);
  }

  public static void log(Object object) {
    System.out.println(object.toString());
  }

  private Logger() {
  }

}
