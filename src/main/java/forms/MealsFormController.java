package forms;

import data.DataAdministration;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import utils.Meal;

/**
 * FXML Controller class
 *
 * @author Rafal Waremczuk
 */
public class MealsFormController implements Initializable {
    
    private Meal selectedItem;
    private static ObservableList<Meal> mealsList;
    
    @FXML
    private Button deleteBtn;

    @FXML
    private Button editBtn;
    
    @FXML
    private TableView<Meal> mealsTable;
    
    @FXML
    private TableColumn<Meal, String> mealNameTableColumn;
    
    @FXML
    private TableColumn<Meal, Integer> productsQuantityTableColumn;

    @FXML
    private TableColumn<Meal, Integer> portionWeightTableColumn;
    
    @FXML
    private TableColumn<Meal, Double> glycemicLoadTableColumn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mealsList = FXCollections.observableArrayList(DataAdministration.getMeals());
        selectedItem = null;
        diableButtons();
        
        initTable();
    }
    
    // FXML methods
    
    // methods
    private void initTable() {
        mealNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("mealName"));
        productsQuantityTableColumn.setCellValueFactory(new PropertyValueFactory<>("productsQuantity"));
        portionWeightTableColumn.setCellValueFactory(new PropertyValueFactory<>("portionWeight"));
        glycemicLoadTableColumn.setCellValueFactory(new PropertyValueFactory<>("glycemicLoad"));
        
        
        mealsTable.setItems(mealsList);

        mealsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                enableButtnos();
                selectedItem = newValue;
            } else {
                diableButtons();
                selectedItem = null;
            }
        });
    }
    
    private void enableButtnos() {
        editBtn.setDisable(false);
        deleteBtn.setDisable(false);
    }
    
    private void diableButtons() {
        editBtn.setDisable(true);
        deleteBtn.setDisable(true);
    }
}
