package Inventory;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;


/**
 *  Product Class is part of the model
 *
 * @author Tim McHale
 *
 */
public class Product {

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price = 0.0;
    private int stock = 0;
    private int min;
    private int max;

    /**
     * Constructor for Product
     * @param id takes int input for id
     * @param name takes string input for Name
     * @param price takes double input for price
     * @param stock takes int input fo stock
     * @param min takes int input for min
     * @param max takes int input for max
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        setId(id);
        setName(name);
        setPrice(price);
        setStock(stock);
        setMin(min);
        setMax(max);
    }

    /**
     * setter for id
     * @param id int input
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * setter for name
     * @param name string input
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * setter for price
     * @param price takes double input
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * setter for stock
     * @param stock takes int input
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * setter for min
     * @param min takes int input
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * setter for max
     * @param max takes int input
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * getter for id
     * @return returns an int
     */
    public int getId() {
        return this.id;
    }

    /**
     *  getter for name
     * @return returns a string
     */
    public String getName() {
        return this.name;
    }

    /**
     * getter for price
     * @return a double
     */
    public double getPrice() {
        return this.price;
    }

    /**
     * getter for stock
     * @return an int
     */
    public int getStock() {
        return this.stock;
    }

    /**
     * getter for min
     * @return an int
     */
    public int getMin() {
        return this.min;
    }

    /**
     * getter for max
     * @return a int
     */
    public int getMax() {
        return this.max;
    }

    /**
     * adds a part to the associated parts data structure
     * @param partToAdd takes a part object
     */
    public void addAssociatedPart(Part partToAdd) {
        associatedParts.add(partToAdd);
    }

    /**
     * deletes a part from the associated parts data structure
     * @param selectedAssociatedPart
     * @return true if part is deleted
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        int i;
        for (i = 0; i < associatedParts.size(); i++) {
            if (associatedParts.get(i).getId() == selectedAssociatedPart.getId()) {
                associatedParts.remove(i);
                return true;
            }
        }

        return false;
    }

    /**
     * getter for the associatedParts data structure
     * @return returns associatedParts FXCollections.observableArrayList
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }

}
