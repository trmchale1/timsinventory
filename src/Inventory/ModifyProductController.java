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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.control.cell.PropertyValueFactory;

/**
 *  ModifyProductController allows you to modify a Product in the UI and handles input.
 *
 *  Run time errors when adding non integer values, fixed in the saveProduct method
 *
 * @author Tim McHale
 */
public class ModifyProductController implements Initializable {

    Inventory inv;
    Product product;

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
    private TableView<Part> assocPartsTable;
    @FXML
    private TableView<Part> partSearchTable;
    @FXML
    private TextField search;
    private ObservableList<Part> partsInventory = FXCollections.observableArrayList();
    private ObservableList<Part> partsInventorySearch = FXCollections.observableArrayList();
    private ObservableList<Part> assocPartList = FXCollections.observableArrayList();

    public ModifyProductController(Inventory inv, Product product) {
        this.inv = inv;
        this.product = product;
    }

    /**
     * Initializes the controller class.
     * @param url the url
     * @param rb  the rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateSearchTable();
        setData();
    }

    /**
     * populates the search table
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
     * clears the text field
     * @param event takes user input
     */
    @FXML
    void clearTextField(MouseEvent event) {
        Object source = event.getSource();
        TextField field = (TextField) source;
        field.setText("");
        if (field == search) {
            partSearchTable.setItems(partsInventory);
        }
    }

    /**
    * searches for a product in the modify window
     * @param event takes user input
     */
    @FXML
    private void modifyProductSearch(MouseEvent event) {
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
     * deletes a part
     * @param event takes user input
     */
    @FXML
    private void deletePart(MouseEvent event) {
        Part removePart = assocPartsTable.getSelectionModel().getSelectedItem();
        boolean deleted = false;
        if (removePart != null) {
            boolean remove = confirmationWindow(removePart.getName());
            if (remove) {
                deleted = product.deleteAssociatedPart(removePart);
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
    * adds a part
     * @param event takes user input
     */
    @FXML
    private void addPart(MouseEvent event) {
        Part addPart = partSearchTable.getSelectionModel().getSelectedItem();
        boolean repeatedItem = false;

        if (addPart == null) {
            return;
        } else {
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
            assocPartsTable.setItems(assocPartList);
        }
    }

    /**
     * cancels the modify screen
     * @param event takes user input
     */
    @FXML
    private void cancelModify(MouseEvent event) {
        boolean cancel = AlertMessage.cancel();
        if (cancel) {
            mainScreen(event);
        } else {
            return;
        }
    }

    /**
     * Runtime error, if price, count, min, max are non-int it will run error.
     * Fixed with try catch at line 186
     *
     * @param event handles the saveProduct
     */
    @FXML
    private void saveProduct(MouseEvent event) {
        resetFieldsStyle();
        boolean end = false;
        TextField[] fieldCount = {count, price, min, max};
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
        if (name.getText().trim().isEmpty() || name.getText().trim().toLowerCase().equals("part name")) {
            AlertMessage.errorProduct(4, name);
            return;
        }
        for (int i = 0; i < fieldCount.length; i++) {
            boolean valueError = checkValue(fieldCount[i]);
            if (valueError) {
                end = true;
                break;
            }
            boolean typeError = checkType(fieldCount[i]);
            if (typeError) {
                end = true;
                break;
            }
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
        if (assocPartList.size() == 0) {
            AlertMessage.errorProduct(7, null);
            return;
        }

        saveProduct();
        mainScreen(event);

    }

    /**
     * save the Product with associated parts
     *
     */
    private void saveProduct() {
        Product product = new Product(Integer.parseInt(id.getText().trim()), name.getText().trim(), Double.parseDouble(price.getText().trim()),
                Integer.parseInt(count.getText().trim()), Integer.parseInt(min.getText().trim()), Integer.parseInt(max.getText().trim()));
        for (int i = 0; i < assocPartList.size(); i++) {
            product.addAssociatedPart(assocPartList.get(i));
        }

        inv.updateProduct(product.getId(),product);

    }

    /**
     * Constructor reads from model
     */
    private void setData() {
        ObservableList<Part> all_parts = product.getAllAssociatedParts();
        for (Part p : all_parts) {
            Part part = p;
            if (part != null) {
                assocPartList.add(part);
            }
        }

        TableColumn<Part, Double> costCol = formatPrice();
        assocPartsTable.getColumns().addAll(costCol);

        assocPartsTable.setItems(assocPartList);

        this.name.setText(product.getName());
        this.id.setText((Integer.toString(product.getId())));
        this.count.setText((Integer.toString(product.getStock())));
        this.price.setText((Double.toString(product.getPrice())));
        this.min.setText((Integer.toString(product.getMin())));
        this.max.setText((Integer.toString(product.getMax())));

    }

    /**
     * sets style for characters
     */
    private void resetFieldsStyle() {
        name.setStyle("-fx-border-color: black");
        count.setStyle("-fx-border-color: black");
        price.setStyle("-fx-border-color: black");
        min.setStyle("-fx-border-color: black");
        max.setStyle("-fx-border-color: black");

    }

    /**
     * Are you sure you want to do delete
     * @param name string input
     * @return true if deleted
     */
    private boolean confirmationWindow(String name) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete part");
        alert.setHeaderText("Are you sure you want to delete: " + name);
        alert.setContentText("Click ok to confirm");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * starts the modifyProduct window
     * @param event takes user input
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
     * checks and parses user data
     * @param field textfield type
     * @return true if empty, else print exception
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
        } catch (Exception e) {
            error = true;
            AlertMessage.errorProduct(3, field);
            System.out.println(e);

        }
        return error;
    }

    /**
     * checks the type of price to be sure it is a double
     * @param field
     * @return
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
    * formats the price into a double
     * @return table column
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