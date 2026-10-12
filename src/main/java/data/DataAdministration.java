package data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import logs.LogImportance;
import utils.Meal;
import utils.Product;

/**
 *
 * @author Rafal Waremczuk
 */
public class DataAdministration {

    private static List<Product> myProducts;
    private static List<Meal> meals;
    private static List<String> categories;
    private static final String VERSION;

    static {
        VERSION = "Wersja: 1.5";
        myProducts = new ArrayList<>();
        categories = new ArrayList<>();
        meals = new ArrayList<>();
    }

    // functions
    // adding
    public static void addProcutsToListAndFile(Product product) {
        myProducts.add(product);
        filesWork.FileParser.addProduct(product);
    }

    public static void addCategoryToListAndFile(String category) {
        categories.add(category);
        filesWork.FileParser.addCategory(category);
    }
    
    public static void addMealToListAndFile(Meal meal) {
        meals.add(meal);
    }
    
    // deleting
    public static void deleteProductFromListAndFile(Product product) {
        myProducts.remove(product);
        filesWork.FileParser.updateDataListFile();
    }

    public static void deleteCategoryFromListAndFile(String categoryName) {
        categories.remove(categoryName);
        filesWork.FileParser.updateCategoryFile();
    }
    
    public static void deleteMealFromListAndFile(Meal meal) {
        meals.remove(meal);
    }
    
    // changing
    public static void changeCategoryNameInProducts(String oldCategory, String newCategory) {
        Thread changeCategoryNameThread = new Thread(() -> {
            for (int i = 0; i < categories.size(); i++) {
                if (categories.get(i).equals(oldCategory)) {
                    categories.set(i, newCategory);
                }
            }
        });
        changeCategoryNameThread.start();

        Thread changeCategoriesInListThread = new Thread(() -> {
            for (Product item : myProducts) {
                if (item.getCategory().equals(oldCategory)) {
                    item.setCategory(newCategory);
                }
            }
        });
        changeCategoriesInListThread.start();

        new Thread(() -> {
            try {
                changeCategoryNameThread.join();
            } catch (InterruptedException ex) {
                System.err.println("Cannot join thread -- changeCategoryNameThread -- InterruptedExteption (DataAdministration | changeCategoryNameInProducts) -> " + ex);
                logs.LogParser.parseLogToFile(LogImportance.CRITICAL, "Cannot join thread -- changeCategoryNameThread -- InterruptedExteption (DataAdministration | changeCategoryNameInProducts) -> " + ex);
            } catch (Exception ex) {
                System.err.println("Cannot join thread -- changeCategoryNameThread -- Other Exception (DataAdministration | changeCategoryNameInProducts) -> " + ex);
                logs.LogParser.parseLogToFile(LogImportance.CRITICAL, "Cannot join thread -- changeCategoryNameThread -- Other Exception (DataAdministration | changeCategoryNameInProducts) -> " + ex);
            }

            filesWork.FileParser.updateCategoryFile();
        }).start();

        new Thread(() -> {
            try {
                changeCategoriesInListThread.join();
            } catch (InterruptedException ex) {
                System.err.println("Cannot join thread -- changeCategoriesInListThread -- InterruptedExteption (DataAdministration | changeCategoryNameInProducts) -> " + ex);
                logs.LogParser.parseLogToFile(LogImportance.CRITICAL, "Cannot join thread -- changeCategoriesInListThread -- InterruptedExteption (DataAdministration | changeCategoryNameInProducts) -> " + ex);
            } catch (Exception ex) {
                System.err.println("Cannot join thread -- changeCategoriesInListThread -- Other Exception (DataAdministration | changeCategoryNameInProducts) -> " + ex);
                logs.LogParser.parseLogToFile(LogImportance.CRITICAL, "Cannot join thread -- changeCategoriesInListThread -- Other Exception (DataAdministration | changeCategoryNameInProducts) -> " + ex);
            }

            filesWork.FileParser.updateDataListFile();
        }).start();

    }

    // Returnals
    public static Map<String, Integer> createCategoryAndQuantityMap() {
        return categories.stream().collect(Collectors.toMap(categoryItem -> categoryItem, categoryItem -> (int) myProducts.stream().filter(item -> item.getCategory().equals(categoryItem)).count()));
    }

    public static boolean canCreateCategory(String categoryName) {

        for (String product : categories) {
            if (product.equals(categoryName)) {
                return false;
            }
        }
        return true;
    }

    // getters
    public static Product getProductByName(String name) {
        return myProducts.stream().filter(item -> item.getName().equals(name)).findAny().orElse(null);
    }
    
    public static List<Product> getMyProducts() {
        return myProducts;
    }

    public static List<Meal> getMeals() {
        return meals;
    }
    

    public static List<String> getCategories() {
        return categories;
    }

    public static String getVERSION() {
        return VERSION;
    }

}
