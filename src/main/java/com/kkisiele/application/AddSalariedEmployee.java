package com.kkisiele.application;

import com.kkisiele.domain.Employee;
import com.kkisiele.domain.EmployeeFactory;
import com.kkisiele.infrastructure.InMemoryDatabase;

public class AddSalariedEmployee implements Transaction {
    private final int empId;
    private final String name;
    private final String address;
    private final double salary;

    public AddSalariedEmployee(int empId, String name, String address, double salary) {
        this.empId = empId;
        this.name = name;
        this.address = address;
        this.salary = salary;
    }

    @Override
    public void execute() {
        EmployeeFactory factory = new EmployeeFactory();
        Employee employee = factory.createSalariedEmployee(empId, name, address, salary);
        InMemoryDatabase.addEmployee(empId, employee);
    }
}
