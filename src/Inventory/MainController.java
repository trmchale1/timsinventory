package Inventory;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
 *  MainController is called by main and begins the app, buttons and inital user input will be handled by this class.
 *
 *  Run time errors when adding non integer values, fixed in the modifyPart, modifyProduct, addProducts methods
 *
 * @author Tim McHale
 */

public class MainController implements Initializable {

    Inventory inv;

    @FXML
    private TextField partSearchBox;
    @FXML
    private TextField productSearchBox;
    @FXML
    private TableView<Part> partsTable;
    @FXML
    private TableView<Product> productsTable;
    private ObservableList<Product> productInventory = FXCollections.observableArrayList();
    private ObservableList<Part> partInventory = FXCollections.observableArrayList();
    private ObservableList<Product> productInventorySearch = FXCollections.observableArrayList();
    private ObservableList<Part> partsInventorySearch = FXCollections.observableArrayList();

    /**
     * Constructor for MainController, takes an Inventory object as input
     * @param inv
     */
    public MainController(Inventory inv) {
        this.inv = inv;
    }

    /**
     * Initializes the controller class by running generatePartsTable / Products
     * @param url mandatory param
     * @param rb  mandatory param
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        generatePartsTable();
        generateProductsTable();
    }

    /**
     *  Method generatePartsTable, no paramaters, reads Inventory and writes it to the UI
     */
    private void generatePartsTable() {
        System.out.println(inv.getAllParts());
        partInventory.setAll(inv.getAllParts());
        TableColumn<Part, Double> costCol = formatPrice();
        partsTable.setPlaceholder(new Label("Empty  Part Search Returned"));
        productsTable.setPlaceholder(new Label("Empty Product Search Returned"));
        partsTable.getColumns().addAll(costCol);
        partsTable.setItems(partInventory);
        partsTable.refresh();
    }

    /**
     *  Method generateProductsTable, no paramaters, reads Inventory and writes it to the UI
     */
    private void generateProductsTable() {
        productInventory.setAll(inv.getAllProducts());
        TableColumn<Product, Double> costCol = formatPrice();
        productsTable.getColumns().addAll(costCol);
        productsTable.setItems(productInventory);
        productsTable.refresh();
    }

    /**
     * takes an integer as input writes and error pop-up to the UI per the code
     * @param code
     */

    private void errorWindow(int code) {
        if (code == 1) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Empty Inventory!");
            alert.setContentText("There's nothing to select!");
            alert.showAndWait();
        }
        if (code == 2) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Selection");
            alert.setContentText("You must select an item!");
            alert.showAndWait();
        }

    }

    /**
     * method confirmationWindow takes a string and asks the user if they are sure they want to delete
     * @param name
     * @return bool true if deleted
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
     * confirmDelete "You can't delete ..."
     * @param name name of the product to delete
     * @return bool return if true
     */
    private boolean confirmDelete(String name) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete product");
        alert.setHeaderText("You can't delete " + name + " this product still has parts assigned to it!");
        alert.setContentText("Click ok to confirm");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return false;
        } else {
            return false;
        }
    }

    /**
     * infoWindow method returns a message based upon whether a part/product has been deleted
     * @param code error code for if else
     * @param name name of the part/product
     */
    private void infoWindow(int code, String name) {
        if (code != 2) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Confirmed");
            alert.setHeaderText(null);
            alert.setContentText(name + " has been deleted!");

            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("There was an error!");
        }
    }

    /**
     * formatPrice formats the price into a double
     * @param <T> table column
     * @return  table column
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
                    } else {
                        setText("");
                    }
                }
            };
        });
        return costCol;
    }

    /**
    * exitProgram exits the program on user input
     * @param event takes user input
     *
     */
    @FXML
    private void exitProgram(ActionEvent event
    ) {
        Platform.exit();
    }

    /**
     * exitProgramButton will exit the program when the button is clicked
     * @param event takes user input
     */
    @FXML
    private void exitProgramButton(MouseEvent event
    ) {
        Platform.exit();
    }

    /**
     * searchForPart searches for a Part
     * @param event takes a mouse to start the search
     */
    @FXML
    private void searchForPart(MouseEvent event) {
        if (!partSearchBox.getText().trim().isEmpty()) {
            partsInventorySearch.clear();
            try {
                int productID = Integer.parseInt(partSearchBox.getText().trim());
                for (Part p : inv.getAllParts()) {
                    if (p.getId() == productID) {
                        partsInventorySearch.add(p);
                    }
                }
            } catch(NumberFormatException ex) {
                System.out.println(ex);
            }
            String productName = partSearchBox.getText().trim();
            for (Part p : inv.getAllParts()) {
                if (p.getName().contains(productName)) {
                    partsInventorySearch.add(p);
                }
            }
            partsTable.setItems(partsInventorySearch);
            partsTable.refresh();
        }
    }
    /**
     * searchForProduct searches for a Product
     * @param event takes a mouse to start the search
     */
    @FXML
    private void searchForProduct(MouseEvent event
    ) {
        if (!productSearchBox.getText().trim().isEmpty()) {
            productInventorySearch.clear();
            try {
                int productID = Integer.parseInt(productSearchBox.getText().trim());
                for (Product p : inv.getAllProducts()) {
                    if (p.getId() == productID) {
                        productInventorySearch.add(p);
                    }
                }
            } catch(NumberFormatException ex) {
                System.out.println(ex);
            }
            String productName = productSearchBox.getText().trim();
            for (Product p : inv.getAllProducts()) {
                if (p.getName().contains(productName)) {
                    productInventorySearch.add(p);
                }
            }
            productsTable.setItems(productInventorySearch);
            productsTable.refresh();
        }
    }

    /**
     * clearText clears the textBox
     * @param event takes user input
     */
    @FXML
    void clearText(MouseEvent event
    ) {
        Object source = event.getSource();
        TextField field = (TextField) source;
        field.setText("");
        if (partSearchBox == field) {
            if (partInventory.size() != 0) {
                partsTable.setItems(partInventory);
            }
        }
        if (productSearchBox == field) {
            if (productInventory.size() != 0) {
                productsTable.setItems(productInventory);
            }
        }
    }

    /**
     * addPart adds a part on user input
     * @param event takes user input
     */
    @FXML
    private void addPart(MouseEvent event
    ) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addPart.fxml"));
            AddPartController controller = new AddPartController(inv);

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
     * Runtime errors with modifyPart, if there are buttons
     * that are buttons unreferenced in the modify controller
     *
     * Fixed on line 284
     *
     * @param event handles modifyPart button mouse event
     */

    @FXML
    private void modifyPart(MouseEvent event
    ) {
        try {
            Part selected = partsTable.getSelectionModel().getSelectedItem();
            if (partInventory.isEmpty()) {
                errorWindow(1);
                return;
            }
            if (!partInventory.isEmpty() && selected == null) {
                errorWindow(2);
                return;
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyPart.fxml"));
                ModifyPartController controller = new ModifyPartController(inv, selected);

                loader.setController(controller);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            }
        } catch (IOException e) {
                System.out.println(e.getCause());
        }
    }

    /**
     * Runtime errors with addProduct, if there are buttons
     * that are buttons unreferenced in the addProduct controller
     *
     * @param event handles addProduct button mouse event
     */

    @FXML
    private void addProduct(MouseEvent event
    ) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddProduct.fxml"));
            AddProductController controller = new AddProductController(inv);

            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Runtime errors with modifyProduct, if there are buttons
     * that are buttons unreferenced in the modifyProduct controller
     *
     * @param event handles modifyProduct button mouse event
     */

    @FXML
    private void modifyProduct(MouseEvent event
    ) {
        try {
            Product productSelected = productsTable.getSelectionModel().getSelectedItem();
            if (productInventory.isEmpty()) {
                errorWindow(1);
                return;
            }
            if (!productInventory.isEmpty() && productSelected == null) {
                errorWindow(2);
                return;

            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyProduct.fxml"));
                ModifyProductController controller = new ModifyProductController(inv, productSelected);

                loader.setController(controller);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * deleteProduct deletes a product
     * @param event takes user input
     */
    @FXML
    private void deleteProduct(MouseEvent event
    ) {
        ObservableList<Part> asso_parts;
        Product removeProduct = productsTable.getSelectionModel().getSelectedItem();
        try {
            asso_parts = removeProduct.getAllAssociatedParts();
        } catch(NullPointerException n){
            errorWindow(2);
            return;
        }
        boolean deleted = false;
        System.out.println(removeProduct);
        if (productInventory.isEmpty()) {
            errorWindow(1);
            return;
        }
        if (!asso_parts.isEmpty()) {
            boolean confirm = confirmDelete(removeProduct.getName());
            if (!confirm) {
                return;
            }
        } else {
            if (removeProduct != null) {
                infoWindow(1, removeProduct.getName());
                inv.deleteProduct(removeProduct);
                productInventory.remove(removeProduct);
                productsTable.setItems(productInventory);
                productsTable.refresh();
                } else {
                    infoWindow(2, "");
                }

            }
        }

    /**
     * deletePart deletes a part
     * @param event takes user input
     */
    @FXML
    private void deletePart(MouseEvent event
    ) {
        Part removePart = partsTable.getSelectionModel().getSelectedItem();
        if (partInventory.isEmpty()) {
            errorWindow(1);
            return;
        }
        if (!partInventory.isEmpty() && removePart == null) {
            errorWindow(2);
            return;
        } else {
            boolean confirm = confirmationWindow(removePart.getName());
            if (!confirm) {
                return;
            }
            inv.deletePart(removePart);
            partInventory.remove(removePart);
            partsTable.refresh();

        }
    }

    /**
     * clearTextField clears the textfield
     * @param event takes user input
     */
    private void clearTextField(MouseEvent event) {
        Object source = event.getSource();
        TextField field = (TextField) source;
        field.setText("");
    }


}



