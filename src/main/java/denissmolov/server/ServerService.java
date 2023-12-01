package denissmolov.server;

import static denissmolov.server.ServerConfig.DIRECTORY_PATH;
import static denissmolov.server.ServerConfig.PORT_NUMBER;
import static denissmolov.utils.FileUtils.getPropertiesFromFile;
import static denissmolov.utils.Logger.log;
import java.io.IOException;
import java.util.Map;

public class ServerService {

  public static void main(String[] args) throws IOException {
    ServerConfig config = readConfigFile(args[0]);
    String directoryPath = config.directoryPath();
    int port = config.port();

    waitForMessages(port, directoryPath);
  }

  private static void waitForMessages(int port, String directoryPath) {
    FileProcessorService fileProcessorService = new FileProcessorService();
    log("FileProcessorService is starting");
    fileProcessorService.processMessages(port, directoryPath);
  }

  private static ServerConfig readConfigFile(String configFilePath) throws IOException {
    Map<String, String> properties = getPropertiesFromFile(configFilePath);
    return new ServerConfig(properties.get(DIRECTORY_PATH), Integer.parseInt(properties.get(PORT_NUMBER)));
  }
}
