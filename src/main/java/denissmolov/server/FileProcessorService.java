package denissmolov.server;

import denissmolov.common.Message;
import static denissmolov.utils.FileUtils.createFile;
import static denissmolov.utils.Logger.log;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileProcessorService {

  private static final int THREADS_QUANTITY = 4;

  private ServerSocket serverSocket;
  private final ExecutorService executorService = Executors.newFixedThreadPool(THREADS_QUANTITY);

  public void processMessages(int port, String directoryPath) {
    try {
      serverSocket = new ServerSocket(port);

      while (true) {
        executorService.submit(new FileProcessor(serverSocket.accept(), directoryPath));
      }

    } catch (IOException e) {
      log("An error occurred in FileProcessorService:" + e.getMessage());
    } finally {
      stop();
    }
  }

  public void stop() {
    try {
      if (serverSocket != null) {
        serverSocket.close();
      }
    } catch (IOException e) {
      log("Cannot close a ServerSocket:" + e.getMessage());
    }
  }

  private record FileProcessor(Socket socket, String directoryPath) implements Runnable {

    @Override
    public void run() {
      log("FileProcessor " + Thread.currentThread().getName() + " is running");
      try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())) {
        Message message = (Message) objectInputStream.readObject();
        log("Incoming:" + message);

        createFile(directoryPath, message.fileName(), message.data());
        sendResponse(message);
      } catch (IOException | ClassNotFoundException e) {
        log("FileProcessor failed: " + e.getMessage());
      } finally {
        try {
          socket.close();
        } catch (IOException e) {
          log("Socket cannot be closed");
        }
      }
      log("FileProcessor " + Thread.currentThread().getName() + " is stopping");
    }

    private void sendResponse(Message message) throws IOException {
      PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
      writer.println(message.fileName());
      writer.close();
    }
  }

}
