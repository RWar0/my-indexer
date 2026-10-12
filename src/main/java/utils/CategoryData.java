package utils;

/**
 *
 * @author Rafal Waremczuk
 */
public class CategoryData {

    private String name;
    private int quantity;

    public CategoryData(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}
