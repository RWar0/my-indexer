package logs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Rafal Waremczuk
 */
public class LogParser {

    private static final String LOGS_FOLDER;
    private static final String LOGS_FILE;
    private static LocalDate currentDate;
    private static final int currentDay;
    private static final int currentMonth;
    private static final int currentYear;
    private static boolean created;

    static {
        LOGS_FOLDER = System.getenv("APPDATA") + File.separator + "RWApps"+ File.separator + "MyIndexer" + File.separator + "logs";
        currentDate = LocalDate.now();
        currentDay = currentDate.getDayOfMonth();
        currentMonth = currentDate.getMonthValue();
        currentYear = currentDate.getYear();
        LOGS_FILE = "log-" + currentDay + "-" + currentMonth + "-" + currentYear + ".txt";
        created = false;
    }

    public static void initLogs() {
        createLogsFolder();
        createLogFile();
    }

    private static void createLogsFolder() {
        File databaseFolder = new File(LOGS_FOLDER);
        if (!databaseFolder.exists()) {
            try {
                databaseFolder.mkdir();
            } catch (Exception e) {
                System.err.println("Something goes wrong during creating database!");
            }
        }
    }

    private static void createLogFile() {
        File file = new File(LOGS_FOLDER, LOGS_FILE);
        boolean isExist = file.exists();
        if (!isExist) {
            try {
                file.createNewFile();
                created = true;
                parseLogToFile(LogImportance.INFO, "THE LOG FILE HAS BEEN CREATED!");
            } catch (IOException e) {
                System.err.println("Something gone wrong during creating file: " + LOGS_FILE);
            }
        } else {
            created = true;
        }
    }

    public static void parseLogToFile(LogImportance type, String message) {
        // [HH::mm::ss.nn] LOG MESSAGE
        System.out.println("Log Added!");
        if (created) {
            new Thread(() -> {
                try {
                    PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(getFileToUse(), true)));
                    writer.println("[" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS")) + "] [" + type + "] " + message);
                    writer.close();
                } catch (IOException e) {
                    System.out.println("Error during writting to the " + LOGS_FILE + " file!");
                }
            }).start();
        }
    }
    
    private static String getFileToUse() {
        return LOGS_FOLDER + "/" + LOGS_FILE;
    }
}
