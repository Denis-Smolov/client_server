package denissmolov.utils;

import static denissmolov.utils.Logger.log;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import java.util.stream.Stream;

public class FileUtils {

  private static final String SEPARATOR = FileSystems.getDefault().getSeparator();

  public static String createFilePath(String directoryPath, String fileName) {
    return "%s%s%s".formatted(directoryPath, SEPARATOR, fileName);
  }

  public static Set<String> listFileNamesInDirectory(String directoryPath) throws IOException {
    try (Stream<Path> stream = Files.list(Paths.get(directoryPath))) {
      return stream
          .filter(file -> !Files.isDirectory(file))
          .map(Path::getFileName)
          .map(Path::toString)
          .filter(fileName -> !fileName.startsWith("."))
          .collect(toSet());
    }
  }

  public static Map<String, String> getPropertiesFromFile(String filePath) throws IOException {
    Properties properties = prepareProperties(filePath);
    return properties.stringPropertyNames().stream()
        .collect(toMap(Function.identity(), properties::getProperty));
  }

  public static Map<String, String> getPropertiesFromFileFiltered(String filePath, String regexp) throws IOException {
    Properties properties = prepareProperties(filePath);
    Pattern pattern = Pattern.compile(regexp);

    return properties.stringPropertyNames().stream()
        .filter(value -> pattern.matcher(value).find())
        .collect(toMap(Function.identity(), properties::getProperty));
  }

  private static Properties prepareProperties(String filePath) throws IOException {
    Properties properties = new Properties();
    properties.load(readFile(filePath));
    return properties;
  }

  public static void deleteFile(String filePath) {
    try {
      Files.delete(Paths.get(filePath));
    } catch (IOException e) {
      log("An error occurred, the file " + filePath + " was not deleted");
    }
  }

  public static void createFile(String directoryPath, String fileName, Map<String, String> data) {
    String filePath = createFilePath(directoryPath, fileName);

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
      for (Map.Entry<String, String> entry : data.entrySet()) {
        writer.append(entry.getKey())
            .append("=")
            .append(entry.getValue())
            .append("\n");
      }
    } catch (IOException e) {
      log("Create file attempt failed:" + e.getMessage());
    }
  }

  private static FileInputStream readFile(String filePath) throws FileNotFoundException {
    return new FileInputStream(filePath);
  }

  private FileUtils() {
  }
}
