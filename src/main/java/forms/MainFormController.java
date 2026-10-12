package forms;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import logs.LogImportance;
import logs.LogParser;

/**
 * FXML Controller class
 *
 * @author Rafal Waremczuk
 */
public class MainFormController implements Initializable {

    @FXML
    private AnchorPane mainCenterAnchorPane;

    @FXML
    private AnchorPane sideMenuAnchorPane;

    @FXML
    private BorderPane borderPaneMain;

    @FXML
    private Label versionLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        changeCenterPanel("/forms/DashboardForm.fxml");
        DashboardFormController.setMainController(this);
        versionLabel.setText(data.DataAdministration.getVERSION());
    }

    @FXML
    private void setDashboard() {
        changeCenterPanel("/forms/DashboardForm.fxml");
    }

    @FXML
    private void setList() {
        changeCenterPanel("/forms/ListForm.fxml");
        ListFormController.setMainController(this);
    }

    @FXML
    public void setCalculator() {
        changeCenterPanel("/forms/CalculatorForm.fxml");
    }

    @FXML
    public void setCategories() {
        changeCenterPanel("/forms/CategoriesForm.fxml");
    }
    
    @FXML
    public void setMeals() {
        changeCenterPanel("/forms/MealsForm.fxml");
    }

    @FXML
    private void addProductBtnHandler() {
        try {
            StackPane addWindow = FXMLLoader.load(getClass().getResource("/forms/AddDialogForm.fxml"));
            Scene scene = new Scene(addWindow);

            Stage newStage = new Stage();
            newStage.setTitle("Dodaj produkt");
            newStage.setScene(scene);

            AddDialogFormController.setAddDialogStage(newStage);
            AddDialogFormController.getAddDialogStage().show();

        } catch (IOException ex) {
            System.err.println("Cannot create ADD PRODUCT Dialog Form! - IOException (MainFormController | addProductBtnHandler) -> " + ex);
            LogParser.parseLogToFile(LogImportance.CRITICAL, "Cannot create ADD PRODUCT Dialog Form! - IOException (MainFormController | addProductBtnHandler) -> " + ex);
        } catch (Exception ex) {
            System.err.println("Cannot create ADD PRODUCT Dialog Form! - Other Exception (MainFormController | addProductHandler) -> " + ex);
            LogParser.parseLogToFile(LogImportance.CRITICAL, "Cannot create ADD PRODUCT Dialog Form! - Other Exception (MainFormController | addProductBtnHandler) -> " + ex);
        }
    }

    public void changeCenterPanel(String whichFile) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(whichFile));
        try {
            Node content = loader.load();
            AnchorPane.setTopAnchor(content, 0.0);
            AnchorPane.setBottomAnchor(content, 0.0);
            AnchorPane.setLeftAnchor(content, 0.0);
            AnchorPane.setRightAnchor(content, 0.0);

            mainCenterAnchorPane.getChildren().setAll(content);
        } catch (IOException e) {
            System.err.println("CANNOT CHANGE ANCHOR!");
            LogParser.parseLogToFile(LogImportance.CRITICAL, "Cannot change anchor pane - MainFormController (line 43).");
        }
    }
}
