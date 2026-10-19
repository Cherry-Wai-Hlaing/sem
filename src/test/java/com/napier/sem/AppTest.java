package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    @Test
    void testDisplayEmployee() {
        App app = new App();

        Employee emp = new Employee();
        emp.emp_no = 1;
        emp.first_name = "Kevin";
        emp.last_name = "Chalmers";
        emp.title = "Engineer";
        emp.salary = 55000;
        emp.dept_name = "IT";
        emp.manager = "John";

        assertDoesNotThrow(() -> app.displayEmployee(emp));
    }

    @Test
    void testDisplayEmployeeNull() {
        App app = new App();

        assertDoesNotThrow(() -> app.displayEmployee(null));
    }

    @Test
    void testPrintSalaries() {
        App app = new App();

        ArrayList<Employee> employees = new ArrayList<>();

        Employee emp = new Employee();
        emp.emp_no = 1;
        emp.first_name = "Kevin";
        emp.last_name = "Chalmers";
        emp.salary = 55000;

        employees.add(emp);

        assertDoesNotThrow(() -> app.printSalaries(employees));
    }
    @Test
    void testPrintSalariesEmpty() {
        App app = new App();

        assertDoesNotThrow(
                () -> app.printSalaries(new ArrayList<>())
        );
    }

    @Test
    void testPrintSalariesNull() {
        App app = new App();

        assertDoesNotThrow(
                () -> app.printSalaries(null)
        );
    }
}