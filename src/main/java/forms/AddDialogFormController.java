/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package forms;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logs.LogImportance;
import utils.Product;

/**
 * FXML Controller class
 *
 * @author Rafal Waremczuk
 */
public class AddDialogFormController implements Initializable {

    private static Stage addDialogStage;
    private static ListFormController listController;

    private final String getGreen;
    private final String getRed;

    {
        getGreen = "-fx-text-fill: #00bd03";
        getRed = "-fx-text-fill: red";
    }

    @FXML
    private TextField carbohydratesTextField;

    @FXML
    private ChoiceBox<String> categoryChoiceBox;

    @FXML
    private TextField digestibleTextField;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField fiberTextField;

    @FXML
    private TextField glycemicIndexTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField newCategoryTextField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorLabel.setText("");
        updateCategories();
    }

    // FXML functions
    @FXML
    private void addCategoryHandler() {
        if (newCategoryTextField.getText().isBlank()) {
            errorLabel.setStyle(getRed);
            errorLabel.setText("Podaj nazwe kategorii!");
        } else {
            if (data.DataAdministration.canCreateCategory(newCategoryTextField.getText())) {
                data.DataAdministration.addCategoryToListAndFile(newCategoryTextField.getText());
                newCategoryTextField.setText("");
                updateCategories();
                errorLabel.setStyle(getGreen);
                errorLabel.setText("Dodano kategorie!");
            } else {
                errorLabel.setStyle(getRed);
                errorLabel.setText("Istnieje taka kategoria!");
                newCategoryTextField.clear();
            }
        }
    }

    @FXML
    private void closeBtnHandler() {
        addDialogStage.close();
    }

    @FXML
    private void addProductBtnHandler() {
        errorLabel.setStyle(getRed);
        if (nameTextField.getText().isBlank()) {
            errorLabel.setText("Nazwa nie moze byc pusta!");
            return;
        }
        if (categoryChoiceBox.getValue() == null || categoryChoiceBox.getValue().isBlank()) {
            errorLabel.setText("Kategoria nie moze byc pusta!");
            return;
        }

        if (digestibleTextField.getText().isBlank() && (carbohydratesTextField.getText().isBlank() || fiberTextField.getText().isBlank())) {
            errorLabel.setText("Uzupełnij węglowodany oraz błonnik (preferowana opcja) lub węglowodany przyswajalne!");
            return;
        } else {
            if (containsAnyLetter(carbohydratesTextField.getText())) {
                errorLabel.setText("Węglowodany muszą być cyfrą / liczbą!");
                return;
            }
            if (containsAnyLetter(fiberTextField.getText())) {
                errorLabel.setText("Błonnik muszi być cyfrą / liczbą!");
                return;
            }
            if ((carbohydratesTextField.getText().isBlank() || fiberTextField.getText().isBlank()) && containsAnyLetter(digestibleTextField.getText())) {
                errorLabel.setText("Węglowodany przyswajalne muszą być cyfrą / liczbą!");
                return;
            }

        }

        if (glycemicIndexTextField.getText().isBlank()) {
            errorLabel.setText("Indeks glikemiczny nie moze byc pusty!");
            return;
        } else {
            if (containsAnyLetterOrSpecials(glycemicIndexTextField.getText())) {
                errorLabel.setText("Indeks glikemiczny musi być liczbą całkowitą!");
                return;
            }
        }

        try {
            if (carbohydratesTextField.getText().isBlank() || fiberTextField.getText().isBlank()) {
                Product item = new Product(nameTextField.getText(), categoryChoiceBox.getValue(), Double.parseDouble(digestibleTextField.getText().replace(",", ".")), Integer.parseInt(glycemicIndexTextField.getText()));
                data.DataAdministration.addProcutsToListAndFile(item);
            } else {
                Product item = new Product(nameTextField.getText(), categoryChoiceBox.getValue(), Double.parseDouble(carbohydratesTextField.getText().replace(",", ".")), Double.parseDouble(fiberTextField.getText().replace(",", ".")), Integer.parseInt(glycemicIndexTextField.getText()));
                data.DataAdministration.addProcutsToListAndFile(item);
            }

            errorLabel.setStyle(getGreen);
            errorLabel.setText("Produkt \"" + nameTextField.getText() + "\" został dodany !");
        } catch (NumberFormatException ex) {
            System.err.println("Cannot parse value during crating Product to add to data (AddDialogFormController | addProductBtnHandler) -> " + ex);
            logs.LogParser.parseLogToFile(LogImportance.ERROR, "Cannot parse value during crating Product to add to data (AddDialogFormController | addProductBtnHandler) -> " + ex);
            errorLabel.setStyle(getRed);
            errorLabel.setText("Produkt \"" + nameTextField.getText() + "\" NIE został dodany !");
        } catch (Exception ex) {
            System.err.println("Other error during creating object to data (AddDialogFormController | addProductBtnHandler) -> " + ex);
            logs.LogParser.parseLogToFile(LogImportance.ERROR, "Other error during creating object to data (AddDialogFormController | addProductBtnHandler) -> " + ex);
            errorLabel.setStyle(getRed);
            errorLabel.setText("Produkt \"" + nameTextField.getText() + "\" NIE został dodany !");
        }

        nameTextField.setText("");
        categoryChoiceBox.setValue(null);
        digestibleTextField.setText("");
        carbohydratesTextField.setText("");
        fiberTextField.setText("");
        glycemicIndexTextField.setText("");

        if (listController != null) {
            listController.updateTable();
        }
    }

    // Functions
    private void updateCategories() {
        categoryChoiceBox.getItems().clear();
        categoryChoiceBox.getItems().addAll(data.DataAdministration.getCategories());
    }

    private boolean containsAnyLetterOrSpecials(String str) {
        if (str == null || str.isBlank()) {
            return false;
        }

        Pattern pattern = Pattern.compile("[a-zA-Z\\W]");
        Matcher matcher = pattern.matcher(str);

        return matcher.find();
    }

    private boolean containsAnyLetter(String str) {
        if (str == null || str.isBlank()) {
            return false;
        }

        Pattern pattern = Pattern.compile("[a-zA-Z]|[^\\w.,]");
        Matcher matcher = pattern.matcher(str);

        return matcher.find();
    }

    // Getters and setters
    public static void setAddDialogStage(Stage addDialogStage) {
        AddDialogFormController.addDialogStage = addDialogStage;
    }

    public static Stage getAddDialogStage() {
        return addDialogStage;
    }

    public static void setListController(ListFormController listController) {
        AddDialogFormController.listController = listController;
    }

}
