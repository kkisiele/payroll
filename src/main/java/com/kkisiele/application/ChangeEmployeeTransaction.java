package com.kkisiele.application;

import com.kkisiele.domain.Employee;
import com.kkisiele.infrastructure.PayrollDatabase;

public abstract class ChangeEmployeeTransaction implements Transaction {
    private final int empId;

    public ChangeEmployeeTransaction(int empId) {
        this.empId = empId;
    }

    @Override
    public final void execute() {
        Employee e = PayrollDatabase.getEmployee(empId);
        if(e != null) {
            change(e);
        } else {
            throw new RuntimeException("No such employee");
        }
    }

    protected abstract void change(Employee e);
}
