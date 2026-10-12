/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package forms;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Rafal Waremczuk
 */
public class ConfirmChangeCategoryNameDialogController implements Initializable {
    
    private static Stage confirmChangeCategoryNameDialogStage;
    
    private static CategoriesFormController categoriesController;
    
    @FXML
    private Label errorLabel;
    
    @FXML
    private TextField newCategoryNameTextField;
    
    @FXML
    private Button acceptBtn;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorLabel.setText("");
        if(CategoriesFormController.getSelectedItem() != null) {
            newCategoryNameTextField.setText(CategoriesFormController.getSelectedItem().getName());
            acceptBtn.setDisable(false);
        }
        else {
            newCategoryNameTextField.setText("");
            newCategoryNameTextField.setDisable(true);
            errorLabel.setText("Coś poszło nie tak!!!!");
            acceptBtn.setDisable(true);
        }
    }
    
    // FXML functions
    @FXML
    private void handleCloseBtn() {
        confirmChangeCategoryNameDialogStage.close();
    }
    
    @FXML
    private void handleAcceptBtn() {
        if(newCategoryNameTextField.getText().equalsIgnoreCase(CategoriesFormController.getSelectedItem().getName())) {
            confirmChangeCategoryNameDialogStage.close();
        }
        
        if(!newCategoryNameTextField.getText().isBlank()) {
            if(data.DataAdministration.canCreateCategory(newCategoryNameTextField.getText())) {
                data.DataAdministration.changeCategoryNameInProducts(CategoriesFormController.getSelectedItem().getName(), newCategoryNameTextField.getText());
                
                if(categoriesController != null) {
                    categoriesController.updateList();
                }
                
                confirmChangeCategoryNameDialogStage.close();
                
            }
            else {
                errorLabel.setText("Istnieje kategoria o takiej nazwie!");
            }
        } else {
            errorLabel.setText("Nazwa nie może być pusta!");
        }
    }
    
    // getters and setters
    public static Stage getConfirmChangeCategoryNameDialogStage() {
        return confirmChangeCategoryNameDialogStage;
    }
    public static void setConfirmChangeCategoryNameDialogStage(Stage confirmChangeCategoryNameDialogStage) {
        ConfirmChangeCategoryNameDialogController.confirmChangeCategoryNameDialogStage = confirmChangeCategoryNameDialogStage;
    }

    public static void setCategoriesController(CategoriesFormController categoriesController) {
        ConfirmChangeCategoryNameDialogController.categoriesController = categoriesController;
    }

    
    
}
