package utils;

import com.myindexer.App;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javafx.application.Platform;
import javafx.scene.control.Label;
import logs.LogImportance;
import logs.LogParser;

/**
 *
 * @author Rafal Waremczuk
 */
public class CustomMessagesThread extends Thread {

    private Label messageLabel;
    private static List<String> myData;

    public CustomMessagesThread(Label messageLabel) {
        initList();
        this.messageLabel = messageLabel;
    }

    @Override
    public void run() {
        Random rand = new Random();
        try {
            while (App.isRunning()) {
                int number = rand.nextInt(myData.size());
                String message = myData.get(number);
                Platform.runLater(() -> messageLabel.setText(message));
                sleep(6000);
            }
        } catch (InterruptedException ex) {
            System.err.println("Cannot sleep and show message in dashboard - InterruptException (CustomMessagesThread | run) -> " + ex);
            LogParser.parseLogToFile(LogImportance.CRITICAL, "Cannot sleep and show message in dashboard - InterruptException (CustomMessagesThread | run) -> " + ex);
        } catch (Exception ex) {
            System.err.println("Cannot sleep and show message in dashboard - OtherException (CustomMessagesThread | run) -> " + ex);
            LogParser.parseLogToFile(LogImportance.CRITICAL, "Cannot sleep and show message in dashboard - OtherException (CustomMessagesThread | run) -> " + ex);
        }
    }

    private void initList() {
        myData = Arrays.asList(
                "Czy to czas na jakiś świetny posiłek?",
                "Czas coś policzyć!",
                "Czas na przygotowanie czegoś pysznego!!"
        );
    }
}
