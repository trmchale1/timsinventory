package Inventory;

/**
 *    OutSourced extends Part where it will be given a company name
 *
 * @author Tim McHale
 *
 */
public class OutSourced extends Part {

    private String companyName;

    /**
     * Constructor for Outsourced
     * @param id int
     * @param name string
     * @param price double
     * @param stock int
     * @param min int
     * @param max int
     * @param companyName company name of the part for outsourced
     */
    public OutSourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id,name,price,stock,min,max);
        setCompanyName(companyName);
    }

    /**
     * setCompanyName sets the companyName
     * @param companyName string input for the company
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * getter for the companyName
     * @return string companyName
     */
    public String getCompanyName() {
        return companyName;
    }

}
