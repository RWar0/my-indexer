package forms;

import data.DataAdministration;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import logs.LogImportance;
import logs.LogParser;
import utils.Product;

/**
 * FXML Controller class
 *
 * @author Rafal Waremczuk
 */
public class ListFormController implements Initializable {

    private static MainFormController mainController;

    private static ObservableList<Product> productsList;

    private static Product selectedItem;

    @FXML
    private TableView<Product> productsTable;

    @FXML
    private TableColumn<Product, Double> carbohydratesTableColumn;

    @FXML
    private TableColumn<Product, String> categoryTableColumn;

    @FXML
    private TableColumn<Product, Double> digestibleTableColumn;

    @FXML
    private TableColumn<Product, Double> fiberTableColumn;

    @FXML
    private TableColumn<Product, Double> glycemicLoadTableColumn;

    @FXML
    private TableColumn<Product, Integer> glycemicIndexTableColumn;

    @FXML
    private TableColumn<Product, String> nameTableColumn;

    @FXML
    private Button calculatorBtn;

    @FXML
    private Button editBtn;

    @FXML
    private Button deleteBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        productsList = FXCollections.observableArrayList(DataAdministration.getMyProducts());
        selectedItem = null;
        diableButtons();

        initTable();
        updateTable();
    }

    private void initTable() {
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryTableColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        carbohydratesTableColumn.setCellValueFactory(new PropertyValueFactory<>("carbohydrates"));
        fiberTableColumn.setCellValueFactory(new PropertyValueFactory<>("fiber"));
        digestibleTableColumn.setCellValueFactory(new PropertyValueFactory<>("digestibleCarbohydrates"));
        glycemicIndexTableColumn.setCellValueFactory(new PropertyValueFactory<>("glycemicIndex"));
        glycemicLoadTableColumn.setCellValueFactory(new PropertyValueFactory<>("glycemicLoadPer100g"));

        productsTable.setItems(productsList);

        productsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                enableButtnos();
                selectedItem = newValue;
            } else {
                diableButtons();
                selectedItem = null;
            }
        });
    }

    @FXML
    public void updateTable() {
        productsList.clear();
        productsList.addAll(DataAdministration.getMyProducts());
    }
    
    public static void updateTable(List<Product> searchedProducts) {
        productsList.clear();
        productsList.addAll(searchedProducts);
    }

    @FXML
    private void addProductHandler() {
        try {
            StackPane addWindow = FXMLLoader.load(getClass().getResource("/forms/AddDialogForm.fxml"));
            Scene scene = new Scene(addWindow);

            Stage newStage = new Stage();
            newStage.setTitle("Dodaj produkt");
            newStage.setScene(scene);

            AddDialogFormController.setAddDialogStage(newStage);
            AddDialogFormController.getAddDialogStage().show();

            AddDialogFormController.setListController(this);
        } catch (IOException ex) {
            System.err.println("Cannot create ADD PRODUCT Dialog Form! - IOException (ListFormController | addProductHandler) -> " + ex);
            LogParser.parseLogToFile(LogImportance.CRITICAL, "Cannot create ADD PRODUCT Dialog Form! - IOException (ListFormController | addProductHandler) -> " + ex);
        } catch (Exception ex) {
            System.err.println("Cannot create ADD PRODUCT Dialog Form! - Other Exception (ListFormController | addProductHandler) -> " + ex);
            LogParser.parseLogToFile(LogImportance.CRITICAL, "Cannot create ADD PRODUCT Dialog Form! - Other Exception (ListFormController | addProductHandler) -> " + ex);
        }
    }
    
    @FXML
    private void calculatorBtnHandler() {
        if (selectedItem != null) {
            openCalculator();
        }
    }

    @FXML
    private void deleteBtnHandler() {
        Alert alert = new Alert(AlertType.NONE,
                "Czy chcesz usunac:\n" + selectedItem.toAlertForm() + "\n?",
                ButtonType.YES, ButtonType.NO);
        alert.setTitle("Usuwanie produktu");

        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            data.DataAdministration.deleteProductFromListAndFile(selectedItem);
            selectedItem = null;
            updateTable();
        }
    }

    @FXML
    private void editBtnHandler() {
        try {
            StackPane addWindow = FXMLLoader.load(getClass().getResource("/forms/EditDialogForm.fxml"));
            Scene scene = new Scene(addWindow);

            Stage newStage = new Stage();
            newStage.setTitle("Edytuj produkt");
            newStage.setScene(scene);
            
            EditDialogFormController.setEditDialogStage(newStage);
            EditDialogFormController.getEditDialogStage().show();
            
            EditDialogFormController.setEditingProduct(selectedItem);
            EditDialogFormController.setListController(this);
        } catch (IOException ex) {
            System.err.println("Cannot create ADD PRODUCT Dialog Form! - IOException (ListFormController | addProductHandler) -> " + ex);
            LogParser.parseLogToFile(LogImportance.CRITICAL, "Cannot create ADD PRODUCT Dialog Form! - IOException (ListFormController | editBtnHandler) -> " + ex);
        } catch (Exception ex) {
            System.err.println("Cannot create ADD PRODUCT Dialog Form! - Other Exception (ListFormController | addProductHandler) -> " + ex);
            LogParser.parseLogToFile(LogImportance.CRITICAL, "Cannot create ADD PRODUCT Dialog Form! - Other Exception (ListFormController | editBtnHandler) -> " + ex);
        }
    }
    
    @FXML
    private void handleSearchBtn() {
        try {
            StackPane searchWindow = FXMLLoader.load(getClass().getResource("/forms/SearchDialogForm.fxml"));
            Scene scene = new Scene(searchWindow);

            Stage newStage = new Stage();
            newStage.setTitle("Wyszukaj produkt");
            newStage.setScene(scene);
            
            SearchDialogFormController.setSearchDialogFormStage(newStage);
            SearchDialogFormController.getSearchDialogFormStage().show();
        } catch (IOException ex) {
            System.err.println("Cannot create SEARCH PRODUCTS Dialog Form! - IOException (ListFormController | handleSearchBtn) -> " + ex);
            LogParser.parseLogToFile(LogImportance.CRITICAL, "Cannot create SEARCH PRODUCTS Dialog Form! - IOException (ListFormController | handleSearchBtn) -> " + ex);
        } catch (Exception ex) {
            System.err.println("Cannot create SEARCH PRODUCTS Dialog Form! - Other Exception (ListFormController | handleSearchBtn) -> " + ex);
            LogParser.parseLogToFile(LogImportance.CRITICAL, "Cannot create SEARCH PRODUCTS Dialog Form! - Other Exception (ListFormController | handleSearchBtn) -> " + ex);
        }
    }

    private void diableButtons() {
        calculatorBtn.setDisable(true);
        editBtn.setDisable(true);
        deleteBtn.setDisable(true);
    }

    private void enableButtnos() {
        calculatorBtn.setDisable(false);
        editBtn.setDisable(false);
        deleteBtn.setDisable(false);
    }

    private void openCalculator() {
        mainController.setCalculator();
    }

    // getters and setter
    public static void setMainController(MainFormController mainController) {
        ListFormController.mainController = mainController;
    }

    public static Product getSelectedItem() {
        return selectedItem;
    }

    public static ObservableList<Product> getProductsList() {
        return productsList;
    }

    
    
}
