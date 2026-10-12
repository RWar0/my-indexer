/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package forms;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import logs.LogImportance;
import logs.LogParser;
import utils.CategoryData;

/**
 * FXML Controller class
 *
 * @author Rafal Waremczuk
 */
public class CategoriesFormController implements Initializable {

    private static ObservableList<CategoryData> categoriesList;
    private static CategoryData selectedItem;

    private final String getGreen;
    private final String getRed;

    {
        getGreen = "-fx-text-fill: #00bd03";
        getRed = "-fx-text-fill: red";
    }

    @FXML
    private Button deleteBtn;

    @FXML
    private Button changeNameBtn;

    @FXML
    private Label errorLabel;

    @FXML
    private Label errorLabel2;

    @FXML
    private TextField newCategoryTextField;

    @FXML
    private TableColumn<CategoryData, String> nameTableColumn;

    @FXML
    private TableColumn<CategoryData, Integer> quantityTableColumn;

    @FXML
    private TableView<CategoryData> productsTable;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        deleteBtn.setDisable(true);
        changeNameBtn.setDisable(true);

        errorLabel.setText("");
        errorLabel2.setText("");

        selectedItem = null;
        categoriesList = FXCollections.observableArrayList();

        initTable();
        updateList();
    }

    @FXML
    public void updateList() {
        errorLabel.setText("");
        errorLabel2.setText("");
        categoriesList.clear();

        for (Map.Entry<String, Integer> entry : data.DataAdministration.createCategoryAndQuantityMap().entrySet()) {
            categoriesList.add(new CategoryData(entry.getKey(), entry.getValue()));
        }
    }

    @FXML
    private void deleteBtnHandler() {
        errorLabel2.setStyle(getRed);
        errorLabel.setText("");
        if (selectedItem != null) {
            if (selectedItem.getQuantity() > 0) {
                errorLabel2.setText("Aby usunąć kategorię, nie może ona zawierać ŻADNYCH produktów - usuń wszystkie produkty z niej!");
            } else {
                Alert alert = new Alert(Alert.AlertType.NONE,
                        "Czy chcesz usunac kategorie: " + selectedItem.getName() + " ?",
                        ButtonType.YES, ButtonType.NO);
                alert.setTitle("Usuwanie kategorii");

                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    errorLabel2.setStyle(getGreen);
                    errorLabel2.setText("Usunięto poprawnie!");
                    data.DataAdministration.deleteCategoryFromListAndFile(selectedItem.getName());
                    updateList();
                }
            }
        } else {
            errorLabel2.setText("Coś poszło nie tak - nie powinno się to nigdy pojawić, jeżeli pojawi to proszę wyślij :D");
        }
    }

    @FXML
    private void createCategoryBtn() {
        errorLabel.setStyle(getRed);
        errorLabel2.setText("");
        if (!newCategoryTextField.getText().isBlank()) {
            if (data.DataAdministration.canCreateCategory(newCategoryTextField.getText())) {
                errorLabel.setStyle(getGreen);
                errorLabel.setText("Dodano poprawnie!");
                data.DataAdministration.addCategoryToListAndFile(newCategoryTextField.getText());
                updateList();
                newCategoryTextField.clear();
            } else {
                errorLabel.setText("Istnieje taka kategoria!");
                newCategoryTextField.clear();
            }
        } else {
            errorLabel.setText("Uzupełnij nazwę kategorii!");
        }
    }

    @FXML
    private void changeNameBtnHandler() {
        if (selectedItem != null) {
            try {
                StackPane confirmWindow = FXMLLoader.load(getClass().getResource("/forms/ConfirmChangeCategoryNameDialog.fxml"));
                Scene scene = new Scene(confirmWindow);

                Stage newStage = new Stage();
                newStage.setTitle("Edytuj nazwe kategorii");
                newStage.setScene(scene);

                ConfirmChangeCategoryNameDialogController.setConfirmChangeCategoryNameDialogStage(newStage);
                ConfirmChangeCategoryNameDialogController.getConfirmChangeCategoryNameDialogStage().show();

                ConfirmChangeCategoryNameDialogController.setCategoriesController(this);
            } catch (IOException ex) {
                System.err.println("Cannot create EDIT CATEGORY NAME Dialog Form! - IOException (CategoriesFormController | changeNameBtnHandler) -> " + ex);
                LogParser.parseLogToFile(LogImportance.CRITICAL, "Cannot create EDIT CATEGORY NAME Dialog Form! - IOException (CategoriesFormController | changeNameBtnHandler) -> " + ex);
            } catch (Exception ex) {
                System.err.println("Cannot create EDIT CATEGORY NAME Dialog Form! - Other Exception (CategoriesFormController | changeNameBtnHandler) -> " + ex);
                LogParser.parseLogToFile(LogImportance.CRITICAL, "Cannot create EDIT CATEGORY NAME Dialog Form! - Other Exception (CategoriesFormController | changeNameBtnHandler) -> " + ex);
            }
        } else {
            errorLabel2.setText("Nie powinno się to pokazać, ale -> Musisz wybrać produkt!");
        }
    }

    private void initTable() {
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityTableColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        productsTable.setItems(categoriesList);

        productsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                deleteBtn.setDisable(false);
                changeNameBtn.setDisable(false);
                selectedItem = newValue;
            } else {
                deleteBtn.setDisable(true);
                changeNameBtn.setDisable(true);
                selectedItem = null;
            }
        });
    }

    //getters and setters
    public static CategoryData getSelectedItem() {
        return selectedItem;
    }

}
