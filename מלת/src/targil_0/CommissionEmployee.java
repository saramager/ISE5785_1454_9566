package targil_0;

/**
 * Represents a commission-based employee, who earns a percentage of their gross sales.
 */
public class CommissionEmployee extends Employee {

    private float grossSales; // Total sales made by the employee
    private int commission;   // Commission percentage (0-100)

    /**
     * Constructs a CommissionEmployee with specified details.
     *
     * @param firstName  The first name of the employee.
     * @param lastName   The last name of the employee.
     * @param id         The employee's ID.
     * @param grossSales The total sales made by the employee.
     * @param commission The commission percentage (0-100).
     * @throws IllegalArgumentException if grossSales is negative or commission is not in [0,100].
     */
    public CommissionEmployee(String firstName, String lastName, int id, float grossSales, int commission) {
        super(firstName, lastName, id);
        if (grossSales < 0) 
            throw new IllegalArgumentException("Gross sales must be non-negative");
        if (commission < 0 || commission > 100) 
            throw new IllegalArgumentException("Commission must be between 0 and 100");
        
        this.grossSales = grossSales;
        this.commission = commission;
    }

    /**
     * Default constructor - initializes with default values.
     */
    public CommissionEmployee() {
        super();
        this.grossSales = 0;
        this.commission = 0;
    }

    /**
     * Gets the total gross sales.
     *
     * @return The gross sales amount.
     */
    public float getGrossSales() {
        return grossSales;
    }

    /**
     * Sets the total gross sales.
     *
     * @param grossSales The new gross sales amount.
     * @throws IllegalArgumentException if grossSales is negative.
     */
    public void setGrossSales(float grossSales) {
        if (grossSales < 0) 
            throw new IllegalArgumentException("Gross sales must be non-negative");
        this.grossSales = grossSales;
    }

    /**
     * Gets the commission percentage.
     *
     * @return The commission percentage.
     */
    public int getCommission() {
        return commission;
    }

    /**
     * Sets the commission percentage.
     *
     * @param commission The new commission percentage (0-100).
     * @throws IllegalArgumentException if commission is not in the range [0, 100].
     */
    public void setCommission(int commission) {
        if (commission < 0 || commission > 100) 
            throw new IllegalArgumentException("Commission must be between 0 and 100");
        this.commission = commission;
    }

    /**
     * Returns a string representation of the commission employee.
     *
     * @return A formatted string containing employee details.
     */
    @Override
    public String toString() {
        return String.format("%s, CommissionEmployee: grossSales=%.2f, commission=%d%%", 
                             super.toString(), grossSales, commission);
    }

    /**
     * Checks if this commission employee is equal to another object.
     *
     * @param obj The object to compare.
     * @return True if the objects are equal, otherwise false.
     */
    @Override
    public boolean equals(Object obj)
    {
     
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        
        CommissionEmployee that = (CommissionEmployee) obj;
        return that.grossSales == this.grossSales &&that.commission == this.commission ;    }

    /**
     * Calculates the earnings of the commission employee.
     *
     * @return The earnings, calculated as (commission / 100) * grossSales.
     */
    @Override
    public float earnings() {
        return (commission / 100.0f) * grossSales;
    }
}
