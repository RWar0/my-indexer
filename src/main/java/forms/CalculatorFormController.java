package forms;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import logs.LogImportance;
import utils.Product;

/**
 * FXML Controller class
 *
 * @author Rafal Waremczuk
 */
public class CalculatorFormController implements Initializable {

    private final String getGreen;
    private final String getYellow;
    private final String getRed;

    private static Product product;

    {
        getGreen = "-fx-text-fill: #00f200";
        getYellow = "-fx-text-fill: yellow";
        getRed = "-fx-text-fill: red";
    }

    @FXML
    private TextField carboTextField;

    @FXML
    private TextField digestibleTextField;

    @FXML
    private TextField fiberTextField;

    @FXML
    private TextField glycIxTextField;

    @FXML
    private Label glycLoad2Label;

    @FXML
    private Label glycLoadLabel;

    @FXML
    private Label grams2Label;

    @FXML
    private TextField grams2TextField;

    @FXML
    private Label gramsLabel;

    @FXML
    private TextField gramsTextField;

    @FXML
    private ChoiceBox<String> productList;

    @FXML
    private Label statusLabel1;

    @FXML
    private Label statusLabel2;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new Thread(() -> {
            addProductsToList();
        }).start();

        if (ListFormController.getSelectedItem() != null) {
            product = ListFormController.getSelectedItem();
            productList.setValue(ListFormController.getSelectedItem().getName() + " | IG: " + ListFormController.getSelectedItem().getGlycemicIndex());
        }

        glycLoadLabel.setText("0");
        glycLoad2Label.setText("0");

        statusLabel1.setText("");
        statusLabel2.setText("");
        initActions();
    }

    @FXML
    private void getProductFromDb() {
        if (productList.getValue() != null && !productList.getValue().isBlank()) {
            product = data.DataAdministration.getProductByName(productList.getValue().split("\\|")[0].trim());
        }
        if (!containsAnyLetterOrSpecials(gramsTextField.getText())) {
            updateAllFromList();
        }
    }

    @FXML
    private void updateAllFromList() {
        if (product != null && !gramsTextField.getText().isBlank()) {
            try {
                gramsLabel.setText(gramsTextField.getText() + "g");

                int grams = Integer.parseInt(gramsTextField.getText());
                double load = product.calculateGlycemicLoad(grams);

                DecimalFormat df = new DecimalFormat("#.##");
                String result = df.format(load);

                glycLoadLabel.setText(result);
                setValueAndColor(statusLabel1, load);

            } catch (NumberFormatException ex) {
                System.err.println("Cannot parse value during calculating data (CalculatorFormController | updateAllFromList) -> " + ex);
                logs.LogParser.parseLogToFile(LogImportance.ERROR, "Cannot parse value during calculating data (CalculatorFormController | updateAllFromList) -> " + ex);
            } catch (Exception ex) {
                System.err.println("Other error during calculating data (CalculatorFormController | updateAllFromList) -> " + ex);
                logs.LogParser.parseLogToFile(LogImportance.ERROR, "Other error during calculating data (CalculatorFormController | updateAllFromList) -> " + ex);
            }
        } else if (gramsTextField.getText().isBlank()) {
            gramsLabel.setText(".......g");
            glycLoadLabel.setText("0");
            statusLabel1.setText("");
        } else {
            statusLabel1.setStyle(getRed);
            statusLabel1.setText("Wybierz produkt!");
        }
    }

    private void updateAllFromData() {
        if (!glycIxTextField.getText().isBlank()
                && ((!carboTextField.getText().isBlank() && !fiberTextField.getText().isBlank()) || !digestibleTextField.getText().isBlank())
                && !grams2TextField.getText().isBlank()) {
            if (isCorrectData()) {
                try {
                    int ig = Integer.parseInt(glycIxTextField.getText());
                    int grams = Integer.parseInt(grams2TextField.getText());

                    if (!digestibleTextField.getText().isBlank() && !containsAnyLetter(digestibleTextField.getText())) {
                        double digestible = Double.parseDouble(digestibleTextField.getText().replace(",", "."));

                        double load = (ig * digestible) / 10000 * grams;

                        DecimalFormat df = new DecimalFormat("#.##");
                        String result = df.format(load);

                        glycLoad2Label.setText(result);
                        setValueAndColor(statusLabel2, load);

                    } else {
                        if (!containsAnyLetter(carboTextField.getText()) && !containsAnyLetter(fiberTextField.getText())) {
                            // (glycemicIndex * digestibleCarbohydrates) / 10000 * grams;
                            // digestibleCarbohydrates = carbohydrates - fiber
                            double carbo = Double.parseDouble(carboTextField.getText().replace(",", "."));
                            double fiber = Double.parseDouble(fiberTextField.getText().replace(",", "."));
                            double digestible = carbo - fiber;
                            double load = (ig * digestible) / 10000 * grams;

                            DecimalFormat df = new DecimalFormat("#.##");
                            String result = df.format(load);

                            glycLoad2Label.setText(result);
                            setValueAndColor(statusLabel2, load);
                        }
                    }

                } catch (NumberFormatException ex) {
                    System.err.println("Cannot parse value during calculating data (CalculatorFormController | updateAllFromData) -> " + ex);
                    logs.LogParser.parseLogToFile(LogImportance.ERROR, "Cannot parse value during calculating data (CalculatorFormController | updateAllFromData) -> " + ex);
                } catch (Exception ex) {
                    System.err.println("Other error during calculating data (CalculatorFormController | updateAllFromData) -> " + ex);
                    logs.LogParser.parseLogToFile(LogImportance.ERROR, "Other error during calculating data (CalculatorFormController | updateAllFromData) -> " + ex);
                }
            }
        }
    }

    private void initActions() {
        statusLabel2.setStyle(getRed);

        productList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            getProductFromDb();
        });

        gramsTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (containsAnyLetterOrSpecials(gramsTextField.getText())) {
                
                statusLabel1.setStyle(getRed);
                statusLabel1.setText("Gramatura musi byc liczba calkowita!");
            } else {
                updateAllFromList();
                gramsLabel.setText(gramsTextField.getText() + "g");
            }
        });

        glycIxTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (containsAnyLetterOrSpecials(glycIxTextField.getText())) {
                statusLabel2.setText("IG musi byc liczba calkowita!");
            } else {
                updateAllFromData();
            }
        });

        carboTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (containsAnyLetter(carboTextField.getText())) {
                statusLabel2.setText("Weglowodany musza byc liczba!");
            } else {
                updateAllFromData();
            }
        });

        fiberTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (containsAnyLetter(fiberTextField.getText())) {
                statusLabel2.setText("Blonnik musi byc liczba!");
            } else {
                updateAllFromData();
            }
        });

        digestibleTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (containsAnyLetter(digestibleTextField.getText())) {
                statusLabel2.setText("WP musza byc liczba!");
            } else {
                updateAllFromData();
            }
        });

        grams2TextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (containsAnyLetterOrSpecials(grams2TextField.getText())) {
                statusLabel2.setStyle(getRed);
                statusLabel2.setText("Gramatura musi byc liczba calkowita!");
            } else {
                updateAllFromData();
                grams2Label.setText(grams2TextField.getText() + "g");
            }
        });

    }

    private void addProductsToList() {
        List<String> tempData = data.DataAdministration.getMyProducts().stream().map(item -> item.getName() + " | IG: " + item.getGlycemicIndex()).collect(Collectors.toList());
        productList.getItems().clear();
        productList.getItems().addAll(tempData);
    }

    private void setValueAndColor(Label label, double value) {
        if (value < 0) {
            label.setStyle(getRed);
            label.setText("Niepoprawne, cos poszlo nie tak !");
        } else if (value <= 10) {
            label.setStyle(getGreen);
            label.setText("Niski");
        } else if (value <= 20) {
            label.setStyle(getYellow);
            label.setText("Sredni");
        } else {
            label.setStyle(getRed);
            label.setText("Wysoki");
        }
    }

    private boolean isCorrectData() {
        return !containsAnyLetterOrSpecials(glycIxTextField.getText()) && !containsAnyLetter(carboTextField.getText())
                && !containsAnyLetter(fiberTextField.getText()) && !containsAnyLetter(digestibleTextField.getText()) && !containsAnyLetterOrSpecials(grams2TextField.getText());
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

}
