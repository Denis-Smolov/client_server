package denissmolov.client;

import static denissmolov.client.DirectoryMonitoring.createMonitoring;
import static denissmolov.utils.Logger.log;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import static java.util.concurrent.TimeUnit.SECONDS;

public class ClientService {

  private static final long INITIAL_DELAY = 2;
  private static final long PERIOD = 20;

  private static final ScheduledExecutorService singleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor();

  public static void main(String[] args) throws Exception {
    DirectoryMonitoring directoryMonitoring = createMonitoring(args[0]);
    startDirectoryMonitoringCronTask(directoryMonitoring);
  }

  private static void startDirectoryMonitoringCronTask(DirectoryMonitoring directoryMonitoring) {
    singleThreadScheduledExecutor.scheduleAtFixedRate(() -> {
      log("\nClientService " + Thread.currentThread().getName());

      try {
        directoryMonitoring.startMonitoring();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }, INITIAL_DELAY, PERIOD, SECONDS);
  }

}
