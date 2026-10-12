package forms;

import data.DataAdministration;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.Product;

/**
 * FXML Controller class
 *
 * @author Rafal Waremczuk
 */
public class SearchDialogFormController implements Initializable {

    private static Stage searchDialogFormStage;

    @FXML
    private ChoiceBox<String> categoryChoiceBox;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField nameTextField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorLabel.setText("");
        categoryChoiceBox.getItems().clear();
        categoryChoiceBox.getItems().addAll(data.DataAdministration.getCategories());
    }

    //FXML methods
    @FXML
    private void handleSearchBtn() {
        if ((nameTextField != null && !nameTextField.getText().isBlank()) || (categoryChoiceBox.getValue() != null && !categoryChoiceBox.getValue().isBlank())) {
            List<Product> searchedProducts = new ArrayList<>();
            if ((nameTextField != null && !nameTextField.getText().isBlank()) && (categoryChoiceBox.getValue() != null && !categoryChoiceBox.getValue().isBlank())) {
                searchedProducts = DataAdministration.getMyProducts().stream().filter(item -> item.getName().contains(nameTextField.getText()) && item.getCategory().equalsIgnoreCase(categoryChoiceBox.getValue())).collect(Collectors.toList());
            } else if (!nameTextField.getText().isBlank()) {
                searchedProducts = DataAdministration.getMyProducts().stream().filter(item -> item.getName().contains(nameTextField.getText())).collect(Collectors.toList());
            } else if (categoryChoiceBox.getValue() != null && !categoryChoiceBox.getValue().isBlank()) {
                    searchedProducts = DataAdministration.getMyProducts().stream().filter(item -> item.getCategory().equalsIgnoreCase(categoryChoiceBox.getValue())).collect(Collectors.toList());
            }

            ListFormController.updateTable(searchedProducts);
            searchDialogFormStage.close();
        } else {
            errorLabel.setText("Musisz wpisać nazwę, wybrać kategorię, albo obydwa!");
        }
    }

    @FXML
    private void handleCancelBtn() {
        searchDialogFormStage.close();
    }

    // getters and setters
    public static Stage getSearchDialogFormStage() {
        return searchDialogFormStage;
    }

    public static void setSearchDialogFormStage(Stage searchDialogFormStage) {
        SearchDialogFormController.searchDialogFormStage = searchDialogFormStage;
    }
    
}
