
package com.napier.sem;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AppIntegrationTest {
    private static App app;

    @BeforeAll
    static void init() {
        app = new App();
        app.connect("localhost:33060", 3000);
    }

    @AfterAll
    static void cleanup() {
        if (app != null) {
            app.disconnect();
        }
    }

    @Test
    void testGetEmployee() {
        Employee emp = app.getEmployee(255530);

        assertNotNull(emp, "Employee should be found");
        assertEquals(255530, emp.emp_no);
        assertEquals("Ronghao", emp.first_name);
        assertEquals("Garigliano", emp.last_name);
    }
}
