package com.kkisiele.application;

import com.kkisiele.domain.Employee;
import com.kkisiele.domain.EmployeeFactory;
import com.kkisiele.infrastructure.InMemoryDatabase;

public class AddHourlyEmployee implements Transaction {
    private final int empId;
    private final String name;
    private final String address;
    private final double hourRate;

    public AddHourlyEmployee(int empId, String name, String address, double hourRate) {
        this.empId = empId;
        this.name = name;
        this.address = address;
        this.hourRate = hourRate;
    }

    @Override
    public void execute() {
        EmployeeFactory factory = new EmployeeFactory();
        Employee employee = factory.createHourlyEmployee(empId, name, address, hourRate);
        InMemoryDatabase.addEmployee(empId, employee);
    }
}
