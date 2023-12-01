package denissmolov.server;

public record ServerConfig(String directoryPath, int port) {

  public static final String DIRECTORY_PATH = "directoryPath";
  public static final String PORT_NUMBER = "port";

}
