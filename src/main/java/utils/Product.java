package utils;

import java.io.Serializable;
import java.text.DecimalFormat;
import logs.LogImportance;
import logs.LogParser;

/**
 * @author Rafal Waremczuk
 */
public class Product implements Serializable {
    private String name;
    private String category;
    private double carbohydrates; // per 100 grams
    private double fiber; // per 100 grams
    
    private double digestibleCarbohydrates; // per 100 grams
    
    private int glycemicIndex; // per 100 grams
    
    private double glycemicLoadPer100g;

    public Product(String name, String category, double carbohydrates, double fiber, int glycemicIndex) {
        this.name = name;
        this.category = category;
        this.carbohydrates = carbohydrates;
        this.fiber = fiber;
        this.glycemicIndex = glycemicIndex;
        
        digestibleCarbohydrates = formatDataToTwoDecimals((carbohydrates - fiber >= 0) ? carbohydrates - fiber : 0);
        
        glycemicLoadPer100g = calculateGlycemicLoad();
    }
    public Product(String name, String category, double digestibleCarbohydrates, int glycemicIndex) {
        this.name = name;
        this.category = category;
        this.digestibleCarbohydrates = digestibleCarbohydrates;
        this.glycemicIndex = glycemicIndex;
        
        carbohydrates = -1;
        fiber = -1;
        
        glycemicLoadPer100g = calculateGlycemicLoad();
    }
    
    // functions
    
    // glycemic load per 100g
    public double calculateGlycemicLoad() {
        return formatDataToTwoDecimals((glycemicIndex * digestibleCarbohydrates) / 100);
    }
    
    // glycemic load per given grams
    public double calculateGlycemicLoad(int grams) {
        return formatDataToTwoDecimals(calculateGlycemicLoad() / 100 * grams);
    }
    
    // editing all data
    public void editAllData(String name, String category, double carbohydrates, double fiber, int glycemicIndex) {
        this.name = name;
        this.category = category;
        this.carbohydrates = carbohydrates;
        this.fiber = fiber;
        this.glycemicIndex = glycemicIndex;
        
        digestibleCarbohydrates = formatDataToTwoDecimals((carbohydrates - fiber >= 0) ? carbohydrates - fiber : 0);
        
        glycemicLoadPer100g = calculateGlycemicLoad();
    }
    
    public void editAllData(String name, String category, double digestibleCarbohydrates, int glycemicIndex) {
        this.name = name;
        this.category = category;
        this.digestibleCarbohydrates = digestibleCarbohydrates;
        this.glycemicIndex = glycemicIndex;
        
        carbohydrates = -1;
        fiber = -1;
        
        glycemicLoadPer100g = calculateGlycemicLoad();
    }
    
    private double formatDataToTwoDecimals(double value) {
        
        DecimalFormat df = new DecimalFormat("#.##");
        String result = df.format(value);
        
        try {
            double returnal = Double.parseDouble(result.replace(",", "."));
            
            return returnal;
        } catch (NumberFormatException ex) {
            System.err.println("Cannot format data to two decimals - NumberFormatException (Product | formatDataToTwoDecimals) -> " + ex);
            LogParser.parseLogToFile(LogImportance.ERROR, "Cannot format data to two decimals - NumberFormatException (Product | formatDataToTwoDecimals) -> " + ex);
            
            return value;
        } catch (Exception ex) {
            System.err.println("Cannot format data to two decimals - Other Exception (Product | formatDataToTwoDecimals) -> " + ex);
            LogParser.parseLogToFile(LogImportance.ERROR, "Cannot format data to two decimals - Other Exception (Product | formatDataToTwoDecimals) -> " + ex);
            
            return value;
        }
    }
    
    // getters and setters
    public String getName() {
        return name;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public double getDigestibleCarbohydrates() {
        return digestibleCarbohydrates;
    }
    public double getCarbohydrates() {
        return carbohydrates;
    }
    public double getFiber() {
        return fiber;
    }
    public int getGlycemicIndex() {
        return glycemicIndex;
    }
    public double getGlycemicLoadPer100g() {
        return glycemicLoadPer100g;
    }
    
    // to file converter
    public String convertToFile() {
        if (carbohydrates >= 0 && fiber >= 0) {
            return name + ";;" + category + ";;" + carbohydrates + ";;" + fiber + ";;" + glycemicIndex;
        } else {
            return name + ";;" + category + ";;" + digestibleCarbohydrates + ";;" + glycemicIndex;
        }
    }
    
    
    public String toAlertForm() {
        return "nazwa: " + name + ", kategoria: " + category + "\nweglowodany: " + carbohydrates + ", blonnik: " + fiber + ", przyswajalne: " + digestibleCarbohydrates + "\nIG: " + glycemicIndex;
    }
    
    
    
}
