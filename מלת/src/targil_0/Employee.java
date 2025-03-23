package targil_0;

/**
 * Abstract class representing an employee in the system.
 */
public abstract class Employee {
    private String firstName;
    private String lastName;
    private int id;

    /**
     * Constructs an Employee object with given details.
     *
     * @param firstName The employee's first name.
     * @param lastName  The employee's last name.
     * @param id        The employee's ID.
     */
    public Employee(String firstName, String lastName, int id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
    }

    /**
     * Default constructor - initializes an employee with generic values.
     */
    public Employee() {
        this("Plony", "Almony", 0);
    }

    /**
     * Returns the employee's first name.
     *
     * @return The first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the employee's first name.
     *
     * @param firstName The new first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the employee's last name.
     *
     * @return The last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the employee's last name.
     *
     * @param lastName The new last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the employee's ID.
     *
     * @return The ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the employee's ID.
     *
     * @param id The new ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns a string representation of the employee.
     *
     * @return A formatted string with employee details.
     */
    @Override
    public String toString() {
        return String.format("Employee: firstName=%s, lastName=%s, id=%d", firstName, lastName, id);
    }

    /**
     * Checks if this employee is equal to another object.
     *
     * @param obj The object to compare.
     * @return True if the objects are equal, otherwise false.
     */
 
 public boolean equals(Employee obj) {
  
     if (obj == null ) return false;
     
  
     return id == obj.id &&
             firstName.equals(obj.firstName) &&
             lastName.equals(obj.lastName);
 }
 /**
  * Abstract method to calculate the employee's earnings.
  *
  * @return The earnings amount.
  */
 
 public abstract float earnings();
  
}
