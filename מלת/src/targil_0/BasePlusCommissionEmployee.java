package targil_0;

/**
 * Represents an employee who earns a base salary plus commission based on sales.
 */
public class BasePlusCommissionEmployee extends CommissionEmployee {

    private float baseSalary; // Fixed base salary for the employee

    /**
     * Constructs a BasePlusCommissionEmployee with specified details.
     *
     * @param firstName  The first name of the employee.
     * @param lastName   The last name of the employee.
     * @param id         The employee's ID.
     * @param grossSales The total sales made by the employee.
     * @param commission The commission percentage (0-100).
     * @param baseSalary The fixed base salary.
     * @throws IllegalArgumentException if grossSales or baseSalary are negative, or if commission is not in [0,100].
     */
    public BasePlusCommissionEmployee(String firstName, String lastName, int id, float grossSales, int commission, float baseSalary) {
        super(firstName, lastName, id, grossSales, commission);
        if (baseSalary < 0) 
            throw new IllegalArgumentException("Base salary must be non-negative");
        
        this.baseSalary = baseSalary;
    }

    /**
     * Default constructor - initializes with default values.
     */
    public BasePlusCommissionEmployee() {
        super();
        this.baseSalary = 0;
    }

    /**
     * Gets the base salary.
     *
     * @return The base salary.
     */
    public float getBaseSalary() {
        return baseSalary;
    }

    /**
     * Sets the base salary.
     *
     * @param baseSalary The new base salary.
     * @throws IllegalArgumentException if baseSalary is negative.
     */
    public void setBaseSalary(float baseSalary) {
        if (baseSalary < 0) 
            throw new IllegalArgumentException("Base salary must be non-negative");
        this.baseSalary = baseSalary;
    }

    /**
     * Returns a string representation of the base plus commission employee.
     *
     * @return A formatted string containing employee details.
     */
    @Override
    public String toString() {
        return String.format("%s, BasePlusCommissionEmployee: baseSalary=%.2f", super.toString(), baseSalary);
    }

    /**
     * Checks if this BasePlusCommissionEmployee is equal to another object.
     *
     * @param obj The object to compare.
     * @return True if the objects are equal, otherwise false.
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;

        BasePlusCommissionEmployee that = (BasePlusCommissionEmployee) obj;
        return that.baseSalary == this.baseSalary;   
    }

    /**
     * Calculates the total earnings of the employee.
     *
     * @return The total earnings, including base salary and commission.
     */
    @Override
    public float earnings() {
        return baseSalary + super.earnings();
    }
}
