package filesWork;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import logs.LogImportance;
import utils.Meal;
import utils.Product;

/**
 * @author Rafal Waremczuk
 */
public class FileParser {

    // ADD TO FILE(category, content, answer) -> content;;answer
    public static void addProduct(Product product) {
        new Thread(() -> {
            File file = new File(FileConnector.getDATA_LIST_FILE());

            try (PrintWriter out = new PrintWriter(new FileWriter(file, true))) {

                out.println(product.convertToFile());

                out.close();
            } catch (IOException ex) {
                System.err.println("OutputStream IOException error (FileParser | addProduct) - " + ex);
                logs.LogParser.parseLogToFile(LogImportance.CRITICAL, "OutputStream error (FileParser | addProduct) - " + ex);
            } catch (Exception ex) {
                System.err.println("OutputStream Other exception (FileParser | addProduct) - " + ex);
                logs.LogParser.parseLogToFile(LogImportance.CRITICAL, "OutputStream Other exception (FileParser | addProduct) - " + ex);
            }
        }).start();
    }

    public static void addCategory(String category) {
        new Thread(() -> {
            File file = new File(FileConnector.getCATEGORY_LIST_FILE());

            try (PrintWriter out = new PrintWriter(new FileWriter(file, true))) {

                out.println(category);

                out.close();
            } catch (IOException ex) {
                System.err.println("OutputStream IOException error (FileParser | addCategory) - " + ex);
                logs.LogParser.parseLogToFile(LogImportance.CRITICAL, "OutputStream error (FileParser | addCategory) - " + ex);
            } catch (Exception ex) {
                System.err.println("OutputStream Other exception (FileParser | addCategory) - " + ex);
                logs.LogParser.parseLogToFile(LogImportance.CRITICAL, "OutputStream Other exception (FileParser | addCategory) - " + ex);
            }
        }).start();
    }

    public static void addMeal(Meal meal) {
        new Thread(() -> {
            File file = new File(FileConnector.getMEALS_LIST_FILE());

            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file, true)) {
                @Override
                protected void writeStreamHeader() throws IOException {
                    reset();
                }
            }) {
                out.writeObject(meal);

                out.close();
            } catch (IOException ex) {
                System.err.println("ObjectOutputStream IOException error (FileParser | addMeal) - " + ex);
                logs.LogParser.parseLogToFile(LogImportance.CRITICAL, "ObjectOutputStream error (FileParser | addMeal) - " + ex);
            } catch (Exception ex) {
                System.err.println("ObjectOutputStream Other exception (FileParser | addMeal) - " + ex);
                logs.LogParser.parseLogToFile(LogImportance.CRITICAL, "ObjectOutputStream Other exception (FileParser | addMeal) - " + ex);
            }

        }).start();
    }

    public static void updateDataListFile() {
        new Thread(() -> {
            File file = new File(FileConnector.getDATA_LIST_FILE());

            try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) {

                for (Product product : data.DataAdministration.getMyProducts()) {
                    out.write(product.convertToFile());
                    out.newLine();
                }
                out.close();
            } catch (IOException ex) {
                System.err.println("BufferedWriter IOException error (FileParser | updateDataListFile) - " + ex);
                logs.LogParser.parseLogToFile(LogImportance.CRITICAL, "OutputStream error (FileParser | updateDataListFile) - " + ex);
            } catch (Exception ex) {
                System.err.println("BufferedWriter Other exception (FileParser | updateDataListFile) - " + ex);
                logs.LogParser.parseLogToFile(LogImportance.CRITICAL, "OutputStream Other exception (FileParser | updateDataListFile) - " + ex);
            }
        }).start();
    }

    public static void updateCategoryFile() {
        new Thread(() -> {
            File file = new File(FileConnector.getCATEGORY_LIST_FILE());

            try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) {

                for (String category : data.DataAdministration.getCategories()) {
                    out.write(category);
                    out.newLine();
                }

                out.close();
            } catch (IOException ex) {
                System.err.println("BufferedWriter IOException error (FileParser | updateCategoryFile) - " + ex);
                logs.LogParser.parseLogToFile(LogImportance.CRITICAL, "BufferedWriter error (FileParser | updateCategoryFile) - " + ex);
            } catch (Exception ex) {
                System.err.println("BufferedWriter Other exception (FileParser | updateCategoryFile) - " + ex);
                logs.LogParser.parseLogToFile(LogImportance.CRITICAL, "BufferedWriter Other exception (FileParser | updateCategoryFile) - " + ex);
            }
        }).start();
    }
    
    public static void updateMealsFile() {
        new Thread(() -> {
            File file = new File(FileConnector.getMEALS_LIST_FILE());

            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
                
                for(Meal item : data.DataAdministration.getMeals()) {
                    out.writeObject(item);
                }
                
                out.close();
            } catch (IOException ex) {
                System.err.println("ObjectOutputStream IOException error (FileParser | updateMealsFile) - " + ex);
                logs.LogParser.parseLogToFile(LogImportance.CRITICAL, "ObjectOutputStream error (FileParser | updateMealsFile) - " + ex);
            } catch (Exception ex) {
                System.err.println("ObjectOutputStream Other exception (FileParser | updateMealsFile) - " + ex);
                logs.LogParser.parseLogToFile(LogImportance.CRITICAL, "ObjectOutputStream Other exception (FileParser | updateMealsFile) - " + ex);
            }
        }).start();
    }

}
