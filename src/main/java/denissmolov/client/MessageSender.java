package denissmolov.client;

import denissmolov.common.Message;
import static denissmolov.utils.Logger.log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MessageSender {

  private Socket socket;
  private ObjectOutputStream objectOutputStream;

  public void startConnection(String ip, int port) {
    try {
      socket = new Socket(ip, port);
      objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
      log("MessageSender connected");
    } catch (IOException e) {
      log("Error when initializing MessageSender connection:" + e);
    }
  }

  public String sendMessageAndGetResult(Message message) throws IOException {
    log("Sending messages to the ServerSocket");
    objectOutputStream.writeObject(message);

    log("Closing socket and terminating program.");

    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    String response = in.readLine();
    log("Response=" + response);

    socket.close();
    return response;
  }

  public void stopConnection() {
    try {
      objectOutputStream.close();
      socket.close();
    } catch (IOException e) {
      log("Error when closing MessageSender:" + e.getMessage());
    }
  }

}
