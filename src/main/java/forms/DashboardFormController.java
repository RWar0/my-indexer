package forms;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import logs.LogImportance;
import logs.LogParser;
import utils.CustomMessagesThread;

/**
 * FXML Controller class
 *
 * @author Rafal Waremczuk
 */
public class DashboardFormController implements Initializable {

    private static MainFormController mainController;
    
    public CustomMessagesThread myCustomMessagesThread;
    
    @FXML
    private Label productsCountLabel;
    
    @FXML
    private Label customMessagesLabel;
    
    @FXML
    private Label categoriesCountLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        productsCountLabel.setText(data.DataAdministration.getMyProducts().size() + "");
        categoriesCountLabel.setText(data.DataAdministration.getCategories().size() + "");
        customMessagesLabel.setText("");
        
        if(myCustomMessagesThread == null || !myCustomMessagesThread.isAlive()) {
            myCustomMessagesThread = new CustomMessagesThread(customMessagesLabel);
            myCustomMessagesThread.start();
        }
        
    }

    @FXML
    private void openListBtnHandler() {
        mainController.changeCenterPanel("/forms/ListForm.fxml");
    }

    @FXML
    private void openCalculatorBtnHandler() {
        mainController.changeCenterPanel("/forms/CalculatorForm.fxml");
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
            System.err.println("Cannot create ADD PRODUCT Dialog Form! - IOException (DashboardFormController | addProductBtnHandler) -> " + ex);
            LogParser.parseLogToFile(LogImportance.CRITICAL, "Cannot create ADD PRODUCT Dialog Form! - IOException (DashboardFormController | addProductBtnHandler) -> " + ex);
        } catch (Exception ex) {
            System.err.println("Cannot create ADD PRODUCT Dialog Form! - Other Exception (DashboardFormController | addProductHandler) -> " + ex);
            LogParser.parseLogToFile(LogImportance.CRITICAL, "Cannot create ADD PRODUCT Dialog Form! - Other Exception (DashboardFormController | addProductBtnHandler) -> " + ex);
        }
    }

    // getters and setters
    public static void setMainController(MainFormController mainController) {
        DashboardFormController.mainController = mainController;
    }

}
