package forms;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
public class EditDialogFormController implements Initializable {

    private final String getGreen;
    private final String getRed;

    {
        getGreen = "-fx-text-fill: #00bd03";
        getRed = "-fx-text-fill: red";
    }

    private static Product editingProduct;

    private static ListFormController listController;

    private static Stage editDialogStage;

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
    
    @FXML
    private Button acceptChangesBtn;
    @FXML
    private Button createCategoryBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorLabel.setText("");
        updateCategories();
        
        editingProduct = ListFormController.getSelectedItem();
        
        if (editingProduct != null) {
            nameTextField.setText(editingProduct.getName());
            categoryChoiceBox.setValue(editingProduct.getCategory());
            if (editingProduct.getCarbohydrates() >= 0 && editingProduct.getFiber() >= 0) {
                carbohydratesTextField.setText(editingProduct.getCarbohydrates() + "");
                fiberTextField.setText(editingProduct.getFiber() + "");
            } else {
                digestibleTextField.setText(editingProduct.getDigestibleCarbohydrates() + "");
            }
            glycemicIndexTextField.setText(editingProduct.getGlycemicIndex() + "");
        }
        
        else {
            nameTextField.setDisable(true);
            
            categoryChoiceBox.setDisable(true);
            newCategoryTextField.setDisable(true);
            createCategoryBtn.setDisable(true);
            
            carbohydratesTextField.setDisable(true);
            fiberTextField.setDisable(true);
            digestibleTextField.setDisable(true);
            glycemicIndexTextField.setDisable(true);
            
            acceptChangesBtn.setDisable(true);
            
            errorLabel.setStyle(getRed);
            errorLabel.setText("Cos poszlo nie tak :/");
        }
    }

    // FXML Functions
    @FXML
    void addCategoryHandler() {
        if (newCategoryTextField.getText().isBlank()) {
            errorLabel.setStyle(getRed);
            errorLabel.setText("Podaj nazwe kategorii!");
        } else {
            data.DataAdministration.addCategoryToListAndFile(newCategoryTextField.getText());
            newCategoryTextField.setText("");
            updateCategories();
            errorLabel.setStyle(getGreen);
            errorLabel.setText("Dodano kategorie!");
        }
    }

    @FXML
    void acceptChangesBtnHandler() {
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
                editingProduct.editAllData(nameTextField.getText(), categoryChoiceBox.getValue(), Double.parseDouble(digestibleTextField.getText().replace(",", ".")), Integer.parseInt(glycemicIndexTextField.getText()));
                filesWork.FileParser.updateDataListFile();
            } else {
                editingProduct.editAllData(nameTextField.getText(), categoryChoiceBox.getValue(), Double.parseDouble(carbohydratesTextField.getText().replace(",", ".")), Double.parseDouble(fiberTextField.getText().replace(",", ".")), Integer.parseInt(glycemicIndexTextField.getText()));
                filesWork.FileParser.updateDataListFile();
            }

            errorLabel.setStyle(getGreen);
            errorLabel.setText("Produkt \"" + nameTextField.getText() + "\" został edytowany !");
            
            listController.updateTable();
            editDialogStage.close();
        } catch (NumberFormatException ex) {
            System.err.println("Cannot parse value during editing Product's data (EditDialogFormController | addProductBtnHandler) -> " + ex);
            logs.LogParser.parseLogToFile(LogImportance.ERROR, "Cannot parse value during editing Product's data (EditDialogFormController | addProductBtnHandler) -> " + ex);
            errorLabel.setStyle(getRed);
            errorLabel.setText("Produkt \"" + nameTextField.getText() + "\" NIE został edytowany !");
        } catch (Exception ex) {
            System.err.println("Other error during editing object's data (EditDialogFormController | addProductBtnHandler) -> " + ex);
            logs.LogParser.parseLogToFile(LogImportance.ERROR, "Other error during editing object's data (EditDialogFormController | addProductBtnHandler) -> " + ex);
            errorLabel.setStyle(getRed);
            errorLabel.setText("Produkt \"" + nameTextField.getText() + "\" NIE został edytowany !");
        }
        
    }

    @FXML
    void closeBtnHandler() {
        editDialogStage.close();
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

    // getters and setters
    public static void setEditDialogStage(Stage editDialogStage) {
        EditDialogFormController.editDialogStage = editDialogStage;
    }

    public static Stage getEditDialogStage() {
        return editDialogStage;
    }

    public static void setListController(ListFormController listController) {
        EditDialogFormController.listController = listController;
    }

    public static Product getEditingProduct() {
        return editingProduct;
    }

    public static void setEditingProduct(Product editingProduct) {
        EditDialogFormController.editingProduct = editingProduct;
    }
    
    

}
