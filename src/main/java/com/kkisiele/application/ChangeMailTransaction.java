package com.kkisiele.application;

import com.kkisiele.domain.Employee;
import com.kkisiele.domain.MailMethod;
import com.kkisiele.infrastructure.InMemoryDatabase;

public class ChangeMailTransaction implements Transaction {
    private final int empId;

    public ChangeMailTransaction(int empId) {
        this.empId = empId;
    }

    @Override
    public void execute() {
        Employee e = InMemoryDatabase.getEmployee(empId);
        if(e != null) {
            e.setMethod(new MailMethod("1 Infinite Loop"));
        }
    }
}
