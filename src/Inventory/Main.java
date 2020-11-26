package Inventory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
*   Main class extends application, this application can be extended to read input from a database that can hold a large database of Parts and Products.
 *
 *
 * @author Tim McHale
 */
public class Main extends Application {


    /**
     * Method start starts UI
     * @param stage is a stage object
     * @throws Exception if loader fails to load
     */
    @Override
    public void start(Stage stage) throws Exception{
        Inventory inv = new Inventory();
        test(inv);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        MainController controller = new MainController(inv);
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     *  test runs some test data
     * @param inv starts empty but then adds test data
     */

    void test(Inventory inv){
        Part a = new InHouse(1, "Alternator", 52.25, 2, 1, 10, 10001);
        Part b = new InHouse(2, "Engine", 24.42, 3, 1, 10, 10003);
        Part c = new InHouse(3, "Control Panel", 87.97, 4, 1, 10, 10002);
        inv.addPart(a);
        inv.addPart(b);
        inv.addPart(c);
        inv.addPart(new InHouse(4, "CPU", 17.20, 7, 1, 10, 10004));
        inv.addPart(new InHouse(5, "GPU", 93.33, 5, 1, 10, 10005));
        //Add OutSourced Parts
        Part e = new OutSourced(6, "DRAM", 54.24, 6, 1, 10, "Rearden Metal");
        Part f = new OutSourced(7, "Resistor", 7.00, 9, 1, 10, "Galt Semiconductor");
        Part g = new OutSourced(8, "Oscillator", 11.32, 1, 1, 10, "d'Anconia Copper");
        inv.addPart(e);
        inv.addPart(f);
        inv.addPart(g);

        Product prod1 = new Product(1, "Electro-Static Generator", 42.24, 2, 1, 10);
        prod1.addAssociatedPart(a);
        prod1.addAssociatedPart(b);
        inv.addProduct(prod1);
        Product prod2 = new Product(2, "Copper Drill", 45.54, 4, 1, 10);
        prod2.addAssociatedPart(c);
        prod2.addAssociatedPart(e);
        inv.addProduct(prod2);
        Product prod3 = new Product(3, "Rearden Steel Track", 7.99, 7, 1, 10);
        prod3.addAssociatedPart(f);
        prod3.addAssociatedPart(g);
        inv.addProduct(prod3);
        Product prod4 = new Product(4, "Motor Car", 21.12, 9, 1, 10);
        inv.addProduct(prod4);
        inv.addProduct(new Product(5, "Tagney Rail Car", 12.21, 6, 1, 10));
    }

    /**
     * Main starts program
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
