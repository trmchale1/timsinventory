package Inventory;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

/**
 *  AddPartController allows you to add a Part in the UI and handles input.
 *
 *  Run time errors when adding non integer values, fixed in the saveAddPart method
 *
 *  @author Tim McHale
 */

public class AddPartController  implements Initializable {
    Inventory inv;

    @FXML
    private RadioButton inHouseRadio;
    @FXML
    private RadioButton outSourcedRadio;
    @FXML
    private TextField id;
    @FXML
    private TextField name;
    @FXML
    private TextField count;
    @FXML
    private TextField price;
    @FXML
    private TextField max;
    @FXML
    private TextField company;
    @FXML
    private Label companyLabel;
    @FXML
    private TextField min;

    /**
     *  copies the inventory into memory
     * @param inv model inventory
     */
    public AddPartController(Inventory inv)  {
        this.inv = inv;
    }

    /**
     * initializes the obj
     * @param url a url
     * @param rb a rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        generatePartID();
        resetFields();
    }

    /**
     * changes the font
     */
    private void resetFields() {
        name.setText("Part Name");
        count.setText("Inv Count");
        price.setText("Part Price");
        min.setText("Min");
        max.setText("Max");
        company.setText("Machine ID");
        companyLabel.setText("Machine ID");
        inHouseRadio.setSelected(true);
    }

    /**
     * generates a part id
     */
    private void generatePartID() {
        boolean match;
        Random randomNum = new Random();
        Integer num = randomNum.nextInt(1000);

        if (inv.getAllParts().isEmpty() == true) {
            id.setText(num.toString());

        }
        if(true == false){

        }
        else {
            match = verifyIfTaken(num);

            if (match == false) {
                id.setText(num.toString());
            } else {
                generatePartID();
            }
        }
    }

    /**
     * verify if taken
     * @param num int input
     * @return true if there is a match
     */
    private boolean verifyIfTaken(Integer num) {
        Part match = inv.lookUpPart(num);
        return match != null;
    }

    /**
     * clears the text field
     * @param event takes user input
     */
    @FXML
    private void clearTextField(MouseEvent event) {
        Object source = event.getSource();
        TextField field = (TextField) source;
        field.setText("");
    }

    /**
     * radio button
     * @param event takes user input
     */
    @FXML
    private void selectInHouse(MouseEvent event) {
        companyLabel.setText("Machine ID");
        company.setText("Machine ID");
    }

    /**
     * radio button
     * @param event takes user input
     */
    @FXML
    private void selectOutSourced(MouseEvent event) {
        companyLabel.setText("Company Name");
        company.setText("Company Name");
    }

    /**
     * disables the field
     * @param event takes user input
     */
    @FXML
    private void idDisabled(MouseEvent event) {
    }

    /**
     * cancels the add part
     * @param event takes user input
     */
    @FXML
    private void cancelAddPart(MouseEvent event) {
        boolean cancel = AlertMessage.cancel();
        if (cancel) {
            mainScreen(event);
        }
    }
    /**
     * Runtime error, if price, count, min, max are non-int it will run error.
     * Fixed with try catch at line 150
     *
     * @param event handles the saveAddPart
     */
    @FXML
    private void saveAddPart(MouseEvent event) {
        resetFieldsStyle();
        boolean end = false;
        TextField[] fieldCount = {count, price, min, max};
        if (inHouseRadio.isSelected() || outSourcedRadio.isSelected()) {
            try {
                int mi = Integer.parseInt(min.getText().trim());
                int ma = Integer.parseInt(max.getText().trim());
                int co = Integer.parseInt(count.getText().trim());
                double p = Double.parseDouble(price.getText().trim());
            } catch(NumberFormatException e) {
                System.out.println(e);
                AlertMessage.errorProduct(1, min);
                return;
            }
            if (name.getText().trim().isEmpty() || name.getText().trim().toLowerCase().equals("part name")) {
                AlertMessage.errorPart(4, name);
                return;
            }
            if (Integer.parseInt(min.getText().trim()) > Integer.parseInt(max.getText().trim())) {
                AlertMessage.errorPart(8, min);
                return;
            }
            if (Integer.parseInt(count.getText().trim()) < Integer.parseInt(min.getText().trim())) {
                AlertMessage.errorPart(6, count);
                return;
            }
            if (Integer.parseInt(count.getText().trim()) > Integer.parseInt(max.getText().trim())) {
                AlertMessage.errorPart(7, count);
                return;
            }
            if (end) {
                return;
            } else if (company.getText().trim().isEmpty() || company.getText().trim().toLowerCase().equals("company name")) {
                AlertMessage.errorPart(3, company);
                return;

            } else if (inHouseRadio.isSelected() && !company.getText().trim().matches("[0-9]*")) {
                AlertMessage.errorPart(9, company);
                return;
            } else if (inHouseRadio.isSelected()) {
                addInHouse();

            } else if (outSourcedRadio.isSelected()) {
                addOutSourced();

            }

        } else {
            AlertMessage.errorPart(2, null);
            return;

        }
        mainScreen(event);
    }

    /**
     * adds a new part with InHouse constructor
     *
     */
    private void addInHouse() {
        inv.addPart(new InHouse(Integer.parseInt(id.getText().trim()), name.getText().trim(),
                Double.parseDouble(price.getText().trim()), Integer.parseInt(count.getText().trim()),
                Integer.parseInt(min.getText().trim()), Integer.parseInt(max.getText().trim()), (Integer.parseInt(company.getText().trim()))));

    }

    /**
     * add new part with OutSourced constructor
     */
    private void addOutSourced() {
        inv.addPart(new OutSourced(Integer.parseInt(id.getText().trim()), name.getText().trim(),
                Double.parseDouble(price.getText().trim()), Integer.parseInt(count.getText().trim()),
                Integer.parseInt(min.getText().trim()), Integer.parseInt(max.getText().trim()), company.getText().trim()));

    }

    /**
     * changes the font
     */
    private void resetFieldsStyle() {
        name.setStyle("-fx-border-color: lightgray");
        count.setStyle("-fx-border-color: lightgray");
        price.setStyle("-fx-border-color: lightgray");
        min.setStyle("-fx-border-color: lightgray");
        max.setStyle("-fx-border-color: lightgray");
        company.setStyle("-fx-border-color: lightgray");
    }

    /**
     * creates the window
     * @param event event obj
     */
    private void mainScreen(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
            MainController controller = new MainController(inv);

            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {

        }
    }

    /**
     * checks the value of price
     * @param field takes a TextField obj
     * @return returns  true if the price obj
     */
    private boolean checkValue(TextField field) {
        boolean error = false;
        try {
            if (field.getText().trim().isEmpty() | field.getText().trim() == null) {
                AlertMessage.errorPart(1, field);
                return true;
            }
            if (field == price && Double.parseDouble(field.getText().trim()) < 0) {
                AlertMessage.errorPart(5, field);
                error = true;
            }
        } catch (Exception e) {
            error = true;
            AlertMessage.errorPart(3, field);
            System.out.println(e);

        }
        return error;
    }

    /**
     * checks the type of price
     * @param field TextField obj
     * @return true if price is a double
     */
    private boolean checkType(TextField field) {

        if (field == price & !field.getText().trim().matches("\\d+(\\.\\d+)?")) {
            AlertMessage.errorPart(3, field);
            return true;
        }
        if (field != price & !field.getText().trim().matches("[0-9]*")) {
            AlertMessage.errorPart(3, field);
            return true;
        }
        return false;
    }


}
