package com.kkisiele.application;

import com.kkisiele.domain.Employee;
import com.kkisiele.infrastructure.InMemoryDatabase;

public class ChangeNameTransaction implements Transaction {
    private final int empId;
    private final String newName;

    public ChangeNameTransaction(int empId, String newName) {
        this.empId = empId;
        this.newName = newName;
    }

    @Override
    public void execute() {
        Employee e = InMemoryDatabase.getEmployee(empId);
        if(e != null) {
            e.setName(newName);
        }
    }
}
