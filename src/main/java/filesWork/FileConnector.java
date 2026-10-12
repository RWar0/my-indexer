package filesWork;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import logs.LogImportance;
import logs.LogParser;
import utils.Meal;
import utils.Product;

/**
 * @author Rafal Waremczuk
 */
public class FileConnector {

    private static final String DATABASE_FOLDER;
    private static final String DATA_LIST_FILE;
    private static final String CATEGORIES_LIST_FILE;
    private static final String MEALS_LIST_FILE;

    static {
        DATABASE_FOLDER = System.getenv("APPDATA") + File.separator + "RWApps"+ File.separator + "MyIndexer" + File.separator + "database";
        DATA_LIST_FILE = DATABASE_FOLDER + File.separator + "MyData.txt";
        CATEGORIES_LIST_FILE = DATABASE_FOLDER + File.separator + "Categories.txt";
        MEALS_LIST_FILE = DATABASE_FOLDER + File.separator + "Meals.dat";
    }
    // Categories to list

    public static void initFiles() {
        createMainDatabaseFolder();
        
        createCategoriesFile();
        createDataListFile();
        createMealsFile();
        
        parseCategoriesFromFileToList();
        parseDataFromFileToList();
        parseMealsFromFileToList();
        
    }
    
    private static void createMainDatabaseFolder() {
        File databaseFolder = new File(DATABASE_FOLDER);
        if (!databaseFolder.exists()) {
            try {
                databaseFolder.mkdirs();
            } catch (Exception e) {
                System.err.println("Something goes wrong during creating database!");
                LogParser.parseLogToFile(LogImportance.CRITICAL,"Error during creating database folder - FileConnector (line 77).");
            }
        }
    }
    
    private static void createDataListFile() {
        File dataFile = new File(DATA_LIST_FILE);
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (Exception e) {
                System.err.println("Something goes wrong during creating data list file!");
                LogParser.parseLogToFile(LogImportance.CRITICAL,"Error during creating data list file - FileConnector (line 59).");
            }
        }
    }
    
    private static void createCategoriesFile() {
        File categoryFile = new File(CATEGORIES_LIST_FILE);
        if (!categoryFile.exists()) {
            try {
                categoryFile.createNewFile();
            } catch (Exception e) {
                System.err.println("Something goes wrong during creating categories list file!");
                LogParser.parseLogToFile(LogImportance.CRITICAL,"Error during creating categories list file - FileConnector (line 71).");
            }
        }
    }
    
    
    private static void createMealsFile() {
        File categoryFile = new File(MEALS_LIST_FILE);
        if (!categoryFile.exists()) {
            try {
                categoryFile.createNewFile();
            } catch (Exception e) {
                System.err.println("Something goes wrong during creating meals list file!");
                LogParser.parseLogToFile(LogImportance.CRITICAL,"Error during creating meals list file - FileConnector (line 84).");
            }
        }
    }
    
    private static void parseDataFromFileToList() {
        File file = new File(DATA_LIST_FILE);

        if (!file.exists() || !file.isFile()) {
            System.out.println("There isn't such a file!");
            return;
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            
            String line;
            while ((line = in.readLine()) != null) {
                String elements[] = line.split(";;");
                // return name + ";;" + category + ";;" + carbohydrates + ";;" + fiber + ";;" + glycemicIndex;
                try {
                    if (elements.length == 5) {
                        data.DataAdministration.getMyProducts().add(new Product(elements[0], elements[1], Double.parseDouble(elements[2]), Double.parseDouble(elements[3]), Integer.parseInt(elements[4])));
                    } else {
                        data.DataAdministration.getMyProducts().add(new Product(elements[0], elements[1], Double.parseDouble(elements[2]), Integer.parseInt(elements[3])));
                    }
                    
                } catch (NumberFormatException ex) {
                    System.err.println("Error during reading data from file - LINE => \"" + line + "\" == " + ex);
                    logs.LogParser.parseLogToFile(LogImportance.ERROR, "Error during reading data from file - LINE => \"" + line + "\" == " + ex);
                }
            }
            
        } catch (IOException ex) {
            System.err.println("Error during opening data list file: " + ex);
            logs.LogParser.parseLogToFile(LogImportance.CRITICAL, "Error during opening data list file (File Connector | parseDataFromFileToList): " + ex);
        } catch (Exception ex) {
            System.err.println("Other error: " + ex);
            logs.LogParser.parseLogToFile(LogImportance.CRITICAL, "Other error (File Connector | parseDataFromFileToList): " + ex);
        }
    }
    
    private static void parseCategoriesFromFileToList() {
        File file = new File(CATEGORIES_LIST_FILE);

        if (!file.exists() || !file.isFile()) {
            System.out.println("There isn't such a file!");
            return;
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            
            String line;
            while ((line = in.readLine()) != null) {
                data.DataAdministration.getCategories().add(line);
            }
            
        } catch (IOException ex) {
            System.err.println("Error during opening category file: " + ex);
            logs.LogParser.parseLogToFile(LogImportance.CRITICAL, "Error during opening category file (File Connector | parseCategoriesFromFileToList): " + ex);
        } catch (Exception ex) {
            System.err.println("Other error: " + ex);
            logs.LogParser.parseLogToFile(LogImportance.CRITICAL, "Other error (File Connector | parseCategoriesFromFileToList): " + ex);
        }
    }
    
    private static void parseMealsFromFileToList() {
        File file = new File(MEALS_LIST_FILE);

        if (!file.exists() || !file.isFile()) {
            System.out.println("There isn't such a file!");
            return;
        }
        
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
                    
            Meal tempMeal;
            while((tempMeal = (Meal) in.readObject()) != null) {
                data.DataAdministration.getMeals().add(tempMeal);
            }
            
            in.close();
        } catch (EOFException ex) {
            
        }catch (IOException ex) {
            System.err.println("Error during opening meals list file: " + ex);
            logs.LogParser.parseLogToFile(LogImportance.CRITICAL, "Error during opening meals list file (File Connector | parseMealsFromFileToList): " + ex);
        } catch (Exception ex) {
            System.err.println("Other error: " + ex);
            logs.LogParser.parseLogToFile(LogImportance.CRITICAL, "Other error (File Connector | parseMealsFromFileToList): " + ex);
        }
    }
    
    // Getters and setters    
    // Getters and setters
    public static String getDATABASE_FOLDER() {
        return DATABASE_FOLDER;
    }

    public static String getDATA_LIST_FILE() {
        return DATA_LIST_FILE;
    }

    public static String getCATEGORY_LIST_FILE() {
        return CATEGORIES_LIST_FILE;
    }

    public static String getMEALS_LIST_FILE() {
        return MEALS_LIST_FILE;
    }
    
}
