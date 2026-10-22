
package com.napier.sem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class App {

    private Connection con;

    // Connect to the database, allowing local or Docker connections.
    public void connect(String location, int delay) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            return;
        }

        int retries = 10;

        for (int i = 0; i < retries; i++) {
            System.out.println("Connecting to database...");

            try {
                if (i > 0) {
                    Thread.sleep(delay);
                }

                con = DriverManager.getConnection(
                        "jdbc:mysql://" + location
                                + "/employees?allowPublicKeyRetrieval=true&useSSL=false",
                        "root",
                        "example"
                );

                System.out.println("Successfully connected");
                return;

            } catch (SQLException e) {
                System.out.println(
                        "Failed to connect to database attempt " + (i + 1)
                );
                System.out.println(e.getMessage());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Database connection interrupted");
                return;
            }
        }

        System.out.println("Could not connect to database.");
    }

    // Retrieve one employee by employee number.
    public Employee getEmployee(int ID) {
        if (con == null) {
            return null;
        }

        String sql =
                "SELECT emp_no, first_name, last_name " +
                        "FROM employees WHERE emp_no = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, ID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Employee emp = new Employee();
                    emp.emp_no = rs.getInt("emp_no");
                    emp.first_name = rs.getString("first_name");
                    emp.last_name = rs.getString("last_name");
                    return emp;
                }
            }

        } catch (SQLException e) {
            System.out.println("Failed to retrieve employee: " + e.getMessage());
        }

        return null;
    }

    // Retrieve employees with a specified title.
    public ArrayList<Employee> getEmployeesByTitle(String title) {
        ArrayList<Employee> employees = new ArrayList<>();

        if (con == null) {
            return employees;
        }

        String sql =
                "SELECT e.emp_no, e.first_name, e.last_name, t.title " +
                        "FROM employees e " +
                        "JOIN titles t ON e.emp_no = t.emp_no " +
                        "WHERE t.title = ? " +
                        "AND t.to_date = '9999-01-01' " +
                        "ORDER BY e.emp_no";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, title);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Employee emp = new Employee();
                    emp.emp_no = rs.getInt("emp_no");
                    emp.first_name = rs.getString("first_name");
                    emp.last_name = rs.getString("last_name");
                    emp.title = rs.getString("title");
                    employees.add(emp);
                }
            }

        } catch (SQLException e) {
            System.out.println("Failed to retrieve employees: " + e.getMessage());
        }

        return employees;
    }

    // Retrieve current salaries for all employees.
    public ArrayList<Employee> getAllSalaries() {
        ArrayList<Employee> employees = new ArrayList<>();

        if (con == null) {
            return employees;
        }

        String sql =
                "SELECT e.emp_no, e.first_name, e.last_name, s.salary " +
                        "FROM employees e " +
                        "JOIN salaries s ON e.emp_no = s.emp_no " +
                        "WHERE s.to_date = '9999-01-01' " +
                        "ORDER BY s.salary DESC, e.emp_no";

        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Employee emp = new Employee();
                emp.emp_no = rs.getInt("emp_no");
                emp.first_name = rs.getString("first_name");
                emp.last_name = rs.getString("last_name");
                emp.salary = rs.getInt("salary");
                employees.add(emp);
            }

        } catch (SQLException e) {
            System.out.println("Failed to retrieve salaries: " + e.getMessage());
        }

        return employees;
    }

    // Retrieve a department by its name.
    public Department getDepartment(String name) {
        if (con == null) {
            return null;
        }

        String sql =
                "SELECT dept_no, dept_name " +
                        "FROM departments WHERE dept_name = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, name);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Department dept = new Department();
                    dept.dept_no = rs.getString("dept_no");
                    dept.dept_name = rs.getString("dept_name");
                    return dept;
                }
            }

        } catch (SQLException e) {
            System.out.println("Failed to retrieve department: " + e.getMessage());
        }

        return null;
    }

    // Retrieve current salaries for employees in a department.
    public ArrayList<Employee> getSalariesByDepartment(Department dept) {
        ArrayList<Employee> employees = new ArrayList<>();

        if (con == null || dept == null) {
            return employees;
        }

        String sql =
                "SELECT e.emp_no, e.first_name, e.last_name, " +
                        "t.title, s.salary, d.dept_name, " +
                        "CONCAT(m.first_name, ' ', m.last_name) AS manager " +
                        "FROM employees e " +
                        "JOIN dept_emp de ON e.emp_no = de.emp_no " +
                        "JOIN departments d ON de.dept_no = d.dept_no " +
                        "JOIN salaries s ON e.emp_no = s.emp_no " +
                        "JOIN titles t ON e.emp_no = t.emp_no " +
                        "LEFT JOIN dept_manager dm " +
                        "ON d.dept_no = dm.dept_no " +
                        "AND dm.to_date = '9999-01-01' " +
                        "LEFT JOIN employees m ON dm.emp_no = m.emp_no " +
                        "WHERE d.dept_no = ? " +
                        "AND de.to_date = '9999-01-01' " +
                        "AND s.to_date = '9999-01-01' " +
                        "AND t.to_date = '9999-01-01' " +
                        "ORDER BY s.salary DESC, e.emp_no";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, dept.dept_no);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Employee emp = new Employee();
                    emp.emp_no = rs.getInt("emp_no");
                    emp.first_name = rs.getString("first_name");
                    emp.last_name = rs.getString("last_name");
                    emp.title = rs.getString("title");
                    emp.salary = rs.getInt("salary");
                    emp.dept_name = rs.getString("dept_name");
                    emp.manager = rs.getString("manager");

                    employees.add(emp);
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Failed to retrieve department salaries: " + e.getMessage()
            );
        }

        return employees;
    }

    // Display a list of employees.
    public void displayEmployees(ArrayList<Employee> employees) {
        if (employees == null || employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }

        for (Employee emp : employees) {
            displayEmployee(emp);
        }
    }

    // Display one employee.
    public void displayEmployee(Employee emp) {
        if (emp == null) {
            System.out.println("Employee not found.");
            return;
        }

        System.out.println("Employee Number: " + emp.emp_no);
        System.out.println("First Name: " + emp.first_name);
        System.out.println("Last Name: " + emp.last_name);

        if (emp.title != null) {
            System.out.println("Title: " + emp.title);
        }

        System.out.println();
    }

    // Print the salary report.
    public void printSalaries(ArrayList<Employee> employees) {
        if (employees == null || employees.isEmpty()) {
            System.out.println("No salary records found.");
            return;
        }

        System.out.printf(
                "%-10s %-15s %-15s %-25s %-12s %-20s %-25s%n",
                "Emp No", "First Name", "Last Name", "Title",
                "Salary", "Department", "Manager"
        );

        for (Employee emp : employees) {
            if (emp == null) {
                continue;
            }

            System.out.printf(
                    "%-10d %-15s %-15s %-25s %-12d %-20s %-25s%n",
                    emp.emp_no,
                    safe(emp.first_name),
                    safe(emp.last_name),
                    safe(emp.title),
                    emp.salary,
                    safe(emp.dept_name),
                    safe(emp.manager)
            );
        }
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    // Close the database connection.
    public void disconnect() {
        if (con != null) {
            try {
                con.close();
                con = null;
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.out.println("Failed to close connection: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        App a = new App();

        if (args.length < 2) {
            a.connect("localhost:33060", 1000);
        } else {
            a.connect(args[0], Integer.parseInt(args[1]));
        }

        // Keep these two lines as required by Lab 07.
        Department dept = a.getDepartment("Development");
        ArrayList<Employee> employees = a.getSalariesByDepartment(dept);

        // Print salary report.
        a.printSalaries(employees);

        // Disconnect from the database.
        a.disconnect();
    }
}
