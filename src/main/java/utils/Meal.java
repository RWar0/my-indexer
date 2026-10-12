package utils;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author Rafal Waremczuk
 */
public class Meal implements Serializable {

    private String mealName;
    private HashMap<Product, Integer> productsMap;

    private int productsQuantity;
    private int portionWeight;
    private double glycemicLoad;

    public Meal(String mealName) {
        this.mealName = mealName;

        productsMap = new HashMap<>();
        productsQuantity = 0;
        glycemicLoad = 0;
        portionWeight = 0;
    }

    public Meal(String mealName, int productsQuantity, int portionWeight, double glycemicLoad, HashMap<Product, Integer> productsMap) {
        this.mealName = mealName;
        this.productsMap = productsMap;
        this.productsQuantity = productsQuantity;
        this.portionWeight = portionWeight;
        this.glycemicLoad = glycemicLoad;
    }

    // methods
    // products
    public void addProduct(Product product, int weight) {
        productsMap.merge(product, weight, Integer::sum);
        productsQuantity++;
        glycemicLoad += product.calculateGlycemicLoad(weight);
        portionWeight += weight;
    }

    public void removeProduct(Product product) {
        int weight = productsMap.get(product);
        glycemicLoad -= product.calculateGlycemicLoad(weight);
        portionWeight -= weight;
        productsQuantity--;

        productsMap.remove(product);
    }

    public void editProduct(Product product, int weight) {
        int oldWeight = productsMap.get(product);
        glycemicLoad -= product.calculateGlycemicLoad(oldWeight);

        productsMap.put(product, weight);
        glycemicLoad += product.calculateGlycemicLoad(weight);
    }

    // glycemic load
    private void recalculateGlycemicLoad() {
        new Thread(() -> {
            glycemicLoad = productsMap.entrySet().stream().mapToDouble(
                    entry -> entry.getKey().calculateGlycemicLoad(entry.getValue())
            ).sum();
        }).start();
    }

    // getters and setters
    private int getProductsQuantity() {
        return productsMap.size();
    }

    public String getMealName() {
        return mealName;
    }
    public HashMap<Product, Integer> getProductsMap() {
        return productsMap;
    }
    public int getPortionWeight() {
        return portionWeight;
    }
    public double getGlycemicLoad() {
        return glycemicLoad;
    }
}
