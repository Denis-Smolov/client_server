package denissmolov.client;

import denissmolov.common.Message;
import static denissmolov.utils.FileUtils.createFilePath;
import static denissmolov.utils.FileUtils.deleteFile;
import static denissmolov.utils.FileUtils.getPropertiesFromFile;
import static denissmolov.utils.FileUtils.getPropertiesFromFileFiltered;
import static denissmolov.utils.FileUtils.listFileNamesInDirectory;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class DirectoryMonitoring {

  private static final String MONITORED_DIRECTORY_PATH = "monitoredDirectoryPath";
  private static final String KEY_FILTERING_PATTERN = "keyFilteringPattern";
  private static final String SERVER_PROGRAM_ADDRESS = "serverProgramAddress";
  private static final String SERVER_PROGRAM_PORT = "serverProgramPort";

  private final String directoryPath;
  private final String regExp;
  private final String serverIp;
  private final int serverPort;

  public DirectoryMonitoring(String directoryPath, String regExp, String serverIp, int serverPort) {
    this.directoryPath = directoryPath;
    this.regExp = regExp;
    this.serverIp = serverIp;
    this.serverPort = serverPort;
  }

  public void startMonitoring() throws IOException {
    Set<String> fileNames = listFileNamesInDirectory(directoryPath);

    for (String fileName : fileNames) {
      String filePath = createFilePath(directoryPath, fileName);

      Map<String, String> data = getPropertiesFromFileFiltered(filePath, regExp);
      if (data.isEmpty() || fileName.equals(relayDataToServer(fileName, data))) {
        deleteFile(filePath);
      }
    }
  }

  public static DirectoryMonitoring createMonitoring(String configFilePath) throws IOException {
    Map<String, String> properties = getPropertiesFromFile(configFilePath);

    return new DirectoryMonitoring(
        properties.get(MONITORED_DIRECTORY_PATH),
        properties.get(KEY_FILTERING_PATTERN),
        properties.get(SERVER_PROGRAM_ADDRESS),
        Integer.parseInt(properties.get(SERVER_PROGRAM_PORT))
    );
  }

  private String relayDataToServer(String fileName, Map<String, String> data) throws IOException {
    MessageSender messageSender = new MessageSender();
    messageSender.startConnection(serverIp, serverPort);
    String response = messageSender.sendMessageAndGetResult(new Message(fileName, data));
    messageSender.stopConnection();
    return response;
  }

}
