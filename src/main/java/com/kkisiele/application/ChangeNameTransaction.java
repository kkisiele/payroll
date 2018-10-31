package com.kkisiele.application;

import com.kkisiele.domain.Employee;

public class ChangeNameTransaction extends ChangeEmployeeTransaction {
    private final String newName;

    public ChangeNameTransaction(int empId, String newName) {
        super(empId);
        this.newName = newName;
    }

    @Override
    protected void change(Employee e) {
        e.setName(newName);
    }
}
