package Inventory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Inventory is the model, it will hold Parts and Products
 *
 * @author Tim McHale
 */

public class Inventory {

    private ObservableList<Part> allParts = FXCollections.observableArrayList();
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * addPart adds a new part to the UI
     * @param newPart takes a part to add
     */
    public void addPart(Part newPart) {
        if (newPart != null) {
            allParts.add(newPart);
        }
    }

    /**
     * addProduct adds a new product to the UI
     * @param newProduct takes a new Product to add
     */
    public void addProduct(Product newProduct) {
        if (newProduct != null) {
            this.allProducts.add(newProduct);
        }
    }

    /**
     * lookupPart, does a brute force search for parts
     * @param partId takes an integer based on index
     * @return a part
     */
    public Part lookUpPart(int partId) {
        if (!allParts.isEmpty()) {
            for (int i = 0; i < allParts.size(); i++) {
                if (allParts.get(i).getId() == partId) {
                    return allParts.get(i);
                }
            }

        }
        return null;
    }

    /**
     * lookupProduct, does a brute force search for products
     * @param productId takes an integer based on index
     * @return a product
     */
    public Product lookUpProduct(int productId) {
        if (!allProducts.isEmpty()) {
            for (int i = 0; i < allProducts.size(); i++) {
                if (allProducts.get(i).getId() == productId) {
                    return allProducts.get(i);
                }
            }
        }
        return null;
    }

    /**
     * lookUpPart looks up parts
     * @param partName takes a string
     * @return a list of Parts
     */
    public ObservableList<Part> lookUpPart(String partName) {
        if (!allParts.isEmpty()) {
            ObservableList searchPartsList = FXCollections.observableArrayList();
            for (Part p : getAllParts()) {
                if (p.getName().contains(partName)) {
                    searchPartsList.add(p);
                }
            }
            return searchPartsList;
        }
        return null;
    }

    /**
     * lookUpProduct looks up a product
     * @param productName takes a string
     * @return the list of products
     */
    public ObservableList<Product> lookUpProduct(String productName) {
        if (!allParts.isEmpty()) {
            ObservableList searchProductsList = FXCollections.observableArrayList();
            for (Product p : getAllProducts()) {
                if (p.getName().contains(productName)) {
                    searchProductsList.add(p);
                }
            }
            return searchProductsList;
        }
        return null;
    }

    /**
     * updatePart updates parts
     * @param index takes an int
     * @param selectedPart takes a part
     */
    public void updatePart(int index,Part selectedPart) {
        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getId() == selectedPart.getId()) {
                allParts.set(i, selectedPart);
                break;
            }
        }
        return;
    }

    /**
     * updateProduct updates a product
     * @param index takes an int
     * @param newProduct takes a product
     */
    public void updateProduct(int index, Product newProduct) {
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getId() == newProduct.getId()) {
                allProducts.set(i, newProduct);
                break;
            }
        }
        return;
    }

    /**
     * deletePart deletes a part
     * @param selectedPart takes a part
     * @return true if deleted
     */
    public boolean deletePart(Part selectedPart) {
        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getId() == selectedPart.getId()) {
                allParts.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * deleteProduct deletes a product
     * @param selectedProduct takes a product
     * @return true if deleted
     */
    public boolean deleteProduct(Product selectedProduct) {
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getId() == selectedProduct.getId()) {
                System.out.print(selectedProduct.getId());
                allProducts.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * getAllParts returns the allParts data structure
     * @return the list of parts
     */
    public ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * getALlProducts returns the allProducts data structure
     * @return the list of products
     */
    public ObservableList<Product> getAllProducts() {
        return allProducts;
    }

}
