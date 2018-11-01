package com.kkisiele.application;

import com.kkisiele.domain.Employee;
import com.kkisiele.domain.HoldMethod;
import com.kkisiele.infrastructure.InMemoryDatabase;

public class ChangeHoldTransaction implements Transaction {
    private final int empId;

    public ChangeHoldTransaction(int empId) {
        this.empId = empId;
    }

    @Override
    public void execute() {
        Employee e = InMemoryDatabase.getEmployee(empId);
        if(e != null) {
            e.setMethod(new HoldMethod());
        }
    }
}
