package Inventory;


/**
 *
 *  InHouse extends Part where it will be given a machine id, part of the model
 *
 * @author Tim McHale
 *
 */
public class InHouse extends Part {

        private int machineId;

    /**
     * Constructor for inHouse
     * @param id takes the id from super
     * @param name takes the name from super
     * @param price takes the price from super
     * @param stock takes the stock from super
     * @param min takes the min from super
     * @param max takes the max from super
     * @param machineId adds a machine id
     */
        public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
            super(id,name,price,stock,min,max);
            setMachineID(machineId);
        }

    /**
     * getMachineID is a getter
     * @return the machineID
     */
    public int getMachineID() {
            return machineId;
        }

    /**
     * setter for machineID
     * @param id returns the id
     */
        public void setMachineID(int id) {
            this.machineId = id;
        }

    }


