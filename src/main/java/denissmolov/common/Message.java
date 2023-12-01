package denissmolov.common;

import java.io.Serializable;
import java.util.Map;

public record Message(String fileName, Map<String, String> data) implements Serializable {

  @Override
  public String toString() {
    return "Message{fileName='" + fileName + "', data=" + data + '}';
  }

}
