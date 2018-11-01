package com.kkisiele.application;

import com.kkisiele.domain.*;
import com.kkisiele.infrastructure.InMemoryDatabase;

public class AddCommissionedEmployee implements Transaction {
    private final int empId;
    private final String name;
    private final String address;
    private final double baseSalary;
    private final double commissionRate;

    public AddCommissionedEmployee(int empId, String name, String address, double baseSalary, double commissionRate) {
        this.empId = empId;
        this.name = name;
        this.address = address;
        this.baseSalary = baseSalary;
        this.commissionRate = commissionRate;
    }

    @Override
    public void execute() {
        EmployeeFactory factory = new EmployeeFactory();
        Employee employee = factory.createCommissionedEmployee(empId, name, address, baseSalary, commissionRate);
        InMemoryDatabase.addEmployee(empId, employee);
    }
}
