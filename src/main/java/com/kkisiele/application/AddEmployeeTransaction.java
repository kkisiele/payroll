package com.kkisiele.application;

import com.kkisiele.domain.*;
import com.kkisiele.infrastructure.PayrollDatabase;

public abstract class AddEmployeeTransaction implements Transaction {
    private final int empId;
    private final String name;
    private final String address;

    public AddEmployeeTransaction(int empId, String name, String address) {
        this.empId = empId;
        this.name = name;
        this.address = address;
    }

    protected abstract PaymentClassification makeClassification();
    protected abstract PaymentSchedule makeSchedule();

    @Override
    public final void execute() {
        PaymentClassification pc = makeClassification();
        PaymentSchedule ps = makeSchedule();
        PaymentMethod pm = new HoldMethod();

        Employee e = new Employee(empId, name, address);
        e.setClassification(pc);
        e.setSchedule(ps);
        e.setMethod(pm);

        PayrollDatabase.addEmployee(empId, e);
    }
}
