package targil_0;

/**
 * Demonstrates payroll processing for different types of employees.
 */
public class Payroll {
    
    public static void main(String[] args) {
        
        // Creating an array of employees
        Employee[] employees = new Employee[3];
        employees[0] = new HourlyEmployee("Alice", "Smith", 326209566, 40, 20);
        employees[1] = new CommissionEmployee("Bob", "Jones", 212075444, 5000, 17);
        employees[2] = new BasePlusCommissionEmployee("Charlie", "Brown", 325287282, 6000, 15, 300);

        // Iterating through employees and displaying their details
        for (Employee emp : employees) {
            System.out.println(emp);
            System.out.printf("Weekly Salary:"+ emp.earnings());

            // Apply a 10% raise for base salary employees
            if (emp instanceof BasePlusCommissionEmployee) {
                BasePlusCommissionEmployee baseEmp = (BasePlusCommissionEmployee) emp;
                baseEmp.setBaseSalary(baseEmp.getBaseSalary() * 1.1f); // Increase by 10%
                System.out.printf(" New Weekly Salary after bonus:" + baseEmp.earnings());
            }
            
            System.out.println(); // Print empty line for better readability
        }
    }
}
