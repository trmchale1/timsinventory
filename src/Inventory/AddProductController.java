package Inventory;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.scene.control.cell.PropertyValueFactory;

/**
 * AddProductController allows you to add a Product in the UI and handles input.
 *
 * Run time errors when adding non integer values, fixed in the saveAddProduct method
 *
 * @author Tim McHale
 */
public class AddProductController implements Initializable {

    Inventory inv;

    @FXML
    private TextField id;
    @FXML
    private TextField name;
    @FXML
    private TextField price;
    @FXML
    private TextField count;
    @FXML
    private TextField min;
    @FXML
    private TextField max;
    @FXML
    private TextField search;
    @FXML
    private TableView<Part> partSearchTable;
    @FXML
    private TableView<Part> assocPartsTable;
    private ObservableList<Part> partsInventory = FXCollections.observableArrayList();
    private ObservableList<Part> partsInventorySearch = FXCollections.observableArrayList();
    private ObservableList<Part> assocPartList = FXCollections.observableArrayList();

    public AddProductController(Inventory inv) {
        this.inv = inv;
    }

    /**
     * Initializes the controller class.
     * @param url a url
     * @param rb a rb
     */

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        generateProductID();
        populateSearchTable();
    }

    /**
     * generates a product id
     */
    private void generateProductID() {
        boolean match;
        Random randomNum = new Random();
        Integer num = randomNum.nextInt(1000);


        if (inv.getAllProducts().isEmpty() == true) {
            id.setText(num.toString());

        }
        if (true == false) {
            AlertMessage.errorProduct(3, null);
        } else {
            match = generateNum(num);

            if (match == false) {
                id.setText(num.toString());
            } else {
                generateProductID();
            }
        }

        id.setText(num.toString());
    }

    /**
     * takes an int and matches a part
     * @param num int input
     * @return true if the part matches
     */
    private boolean generateNum(Integer num) {
        Part match = inv.lookUpPart (num);
        return match != null;
    }


    /**
     * populates the search table
     *
     */
    private void populateSearchTable() {
        partsInventory.setAll(inv.getAllParts());

        TableColumn<Part, Double> costCol = formatPrice();
        partSearchTable.setPlaceholder(new Label("Empty Part Search Returned"));

        partSearchTable.getColumns().addAll(costCol);

        partSearchTable.setItems(partsInventory);
        partSearchTable.refresh();
    }

    /**
     * searches for a part
     * @param event takes user input
     */
    @FXML
    private void searchForPart(MouseEvent event) {
        if (!search.getText().trim().isEmpty()) {
            partsInventorySearch.clear();
            try {
                int productID = Integer.parseInt(search.getText().trim());
                for (Part p : inv.getAllParts()) {
                    if (p.getId() == productID) {
                        partsInventorySearch.add(p);
                    }
                }
            } catch(NumberFormatException ex) {
                System.out.println(ex);
            }
            String productName = search.getText().trim();
            for (Part p : inv.getAllParts()) {
                if (p.getName().contains(productName)) {
                    partsInventorySearch.add(p);
                }
            }
            partSearchTable.setItems(partsInventorySearch);
            partSearchTable.refresh();
        }
    }

    /**
     * adds a part
     * @param event takes user input
     */
    @FXML
    private void addPart(MouseEvent event) {
        Part addPart = partSearchTable.getSelectionModel().getSelectedItem();
        boolean repeatedItem = false;

        if (addPart != null) {
            int id = addPart.getId();
            for (int i = 0; i < assocPartList.size(); i++) {
                if (assocPartList.get(i).getId() == id) {
                    AlertMessage.errorProduct(2, null);
                    repeatedItem = true;
                }
            }

            if (!repeatedItem) {
                assocPartList.add(addPart);

            }

            TableColumn<Part, Double> costCol = formatPrice();
            assocPartsTable.getColumns().addAll(costCol);

            assocPartsTable.setItems(assocPartList);
        }
    }

    /**
     * delete a part
     * @param event takes user input
     */
    @FXML
    private void deletePart(MouseEvent event
    ) {
        Part removePart = assocPartsTable.getSelectionModel().getSelectedItem();
        boolean deleted = false;
        if (removePart != null) {
            boolean remove = AlertMessage.confirmationWindow(removePart.getName());
            if (remove) {
                assocPartList.remove(removePart);
                assocPartsTable.refresh();
            }
        } else {
            return;
        }
        if (deleted) {
            AlertMessage.infoWindow(1, removePart.getName());
        } else {
            AlertMessage.infoWindow(2, "");
        }

    }


    /**
     * cancels the screen
     * @param event takes user input
     */
    @FXML
    private void cancelAddProduct(MouseEvent event
    ) {
        boolean cancel = AlertMessage.cancel();
        if (cancel) {
            mainScreen(event);
        }
    }
    /**
     * Runtime error, if price, count, min, max are non-int it will run error.
     *
     * Fixed with try catch line 209
     *
     * @param event handles the saveAddProduct
     */
    @FXML
    private void saveAddProduct(MouseEvent event
    ) {
        resetFieldsStyle();
        boolean end = false;
        double minCost = 0;
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
        for (int i = 0; i < assocPartList.size(); i++) {
            minCost += assocPartList.get(i).getPrice();
        }
        if (name.getText().trim().isEmpty() || name.getText().trim().toLowerCase().equals("product name")) {
            AlertMessage.errorProduct(4, name);
            return;
        }

        try {
            int mi = Integer.parseInt(min.getText().trim());
        } catch(NumberFormatException e) {
            System.out.println(e);
            AlertMessage.errorProduct(1, min);
            return;
        }

        if (Integer.parseInt(min.getText().trim()) > Integer.parseInt(max.getText().trim())) {
            AlertMessage.errorProduct(10, min);
            return;
        }
        if (Integer.parseInt(count.getText().trim()) < Integer.parseInt(min.getText().trim())) {
            AlertMessage.errorProduct(8, count);
            return;
        }
        if (Integer.parseInt(count.getText().trim()) > Integer.parseInt(max.getText().trim())) {
            AlertMessage.errorProduct(9, count);
            return;
        }
        if (Double.parseDouble(price.getText().trim()) < minCost) {
            AlertMessage.errorProduct(6, price);
            return;
        }
        saveProduct();
        mainScreen(event);
    }

    /**
     * changes the font
     * @param field TextField obj input
     */
    private void fieldError(TextField field) {
        if (field != null) {
            field.setStyle("-fx-border-color: red");
        }
    }

    /**
     * saves the product
     */
    private void saveProduct() {
        Product product = new Product(Integer.parseInt(id.getText().trim()), name.getText().trim(), Double.parseDouble(price.getText().trim()),
                Integer.parseInt(count.getText().trim()), Integer.parseInt(min.getText().trim()), Integer.parseInt(max.getText().trim()));
        for (int i = 0; i < assocPartList.size(); i++) {
            product.addAssociatedPart(assocPartList.get(i));
        }

        inv.addProduct(product);

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
     * clear the text
     * @param event takes user input
     */
    @FXML
    void clearText(MouseEvent event
    ) {
        Object source = event.getSource();
        TextField field = (TextField) source;
        field.setText("");
        if (search == field) {
            if (partsInventory.size() != 0) {
                partSearchTable.setItems(partsInventory);
            }
        }
    }

    /**
     * starts the screen
     * @param event taes user input
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
     * check the value of price
     * @param field textField obj
     * @return true if price is double
     */
    private boolean checkValue(TextField field) {
        boolean error = false;
        try {
            if (field.getText().trim().isEmpty() || field.getText().trim() == null) {
                AlertMessage.errorProduct(1, field);
                return true;
            }
            if (field == price && Double.parseDouble(field.getText().trim()) < 0) {
                AlertMessage.errorProduct(5, field);
                error = true;
            }
        } catch (NumberFormatException e) {
            error = true;
            AlertMessage.errorProduct(3, field);
            System.out.println(e);

        }
        return error;
    }

    /**
     * checks the type of price
     * @param field textField obj
     * @return returns true if price is double
     */
    private boolean checkType(TextField field) {

        if (field == price & !field.getText().trim().matches("\\d+(\\.\\d+)?")) {
            AlertMessage.errorProduct(3, field);
            return true;
        }
        if (field != price & !field.getText().trim().matches("[0-9]*")) {
            AlertMessage.errorProduct(3, field);
            return true;
        }
        return false;

    }

    /**
     * formats the price
     * @param <T> takes a table column
     * @return returns a table column
     */
    private <T> TableColumn<T, Double> formatPrice() {
        TableColumn<T, Double> costCol = new TableColumn("Price");
        costCol.setCellValueFactory(new PropertyValueFactory<>("Price"));
        // Format as currency
        costCol.setCellFactory((TableColumn<T, Double> column) -> {
            return new TableCell<T, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    if (!empty) {
                        setText("$" + String.format("%10.2f", item));
                    }
                    else{
                        setText("");
                    }
                }
            };
        });
        return costCol;
    }

}

