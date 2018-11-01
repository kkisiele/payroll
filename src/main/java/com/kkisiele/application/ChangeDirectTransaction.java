package com.kkisiele.application;

import com.kkisiele.domain.DirectDepositMethod;
import com.kkisiele.domain.Employee;
import com.kkisiele.infrastructure.InMemoryDatabase;

public class ChangeDirectTransaction implements Transaction {
    private final int empId;

    public ChangeDirectTransaction(int empId) {
        this.empId = empId;
    }

    @Override
    public void execute() {
        Employee e = InMemoryDatabase.getEmployee(empId);
        if(e != null) {
            e.setMethod(new DirectDepositMethod("mBank", "123"));
        }
    }
}
