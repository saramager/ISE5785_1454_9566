package targil_0;

/**
 * Represents an hourly employee, which is a type of Employee.
 * An hourly employee is paid based on the number of hours worked and their wage per hour.
 */
public class HourlyEmployee extends Employee {
    
    private int hours; // Number of hours worked
    private float wage; // Wage per hour

    /**
     * Constructs an HourlyEmployee with specified details.
     *
     * @param firstName The first name of the employee.
     * @param lastName  The last name of the employee.
     * @param id        The employee's ID.
     * @param hours     The number of hours worked.
     * @param wage      The hourly wage.
     * @throws IllegalArgumentException if hours or wage are negative.
     */
    public HourlyEmployee(String firstName, String lastName, int id, int hours, float wage) {
        super(firstName, lastName, id);
        if (hours < 0 || wage < 0)
            throw new IllegalArgumentException("Hours and wage cannot be negative");
        this.hours = hours;
        this.wage = wage;
    }

    /**
     * Default constructor - initializes with default values.
     */
    public HourlyEmployee() {
        super();
        this.hours = 0;
        this.wage = 0;
    }

    /**
     * Gets the number of hours worked.
     *
     * @return The number of hours.
     */
    public int getHours() {
        return hours;
    }

    /**
     * Sets the number of hours worked.
     *
     * @param hours The new number of hours.
     * @throws IllegalArgumentException if hours are negative.
     */
    public void setHours(int hours) {
        if (hours < 0)
            throw new IllegalArgumentException("Hours cannot be negative");
        this.hours = hours;
    }

    /**
     * Gets the hourly wage.
     *
     * @return The wage per hour.
     */
    public float getWage() {
        return wage;
    }

    /**
     * Sets the hourly wage.
     *
     * @param wage The new wage.
     * @throws IllegalArgumentException if wage is negative.
     */
    public void setWage(float wage) {
        if (wage < 0)
            throw new IllegalArgumentException("Wage cannot be negative");
        this.wage = wage;
    }

    /**
     * Returns a string representation of the hourly employee.
     *
     * @return A formatted string containing employee details.
     */
    @Override
    public String toString() {
        return String.format("%s, HourlyEmployee: hours=%d, wage=%.2f", super.toString(), hours, wage);
    }

    /**
     * Checks if this hourly employee is equal to another object.
     *
     * @param obj The object to compare.
     * @return True if the objects are equal, otherwise false.
     */
  
    public boolean equals(HourlyEmployee obj)
    {
        if (!super.equals(obj)) return false;
       
        return obj.hours == this.hours &&obj.wage == this.wage ;
    }
    /**
     * Calculates the earnings of the hourly employee.
     *
     * @return The total earnings, calculated as hours * wage.
     */
    @Override
    public float earnings() {
        return hours * wage;
    }
}
