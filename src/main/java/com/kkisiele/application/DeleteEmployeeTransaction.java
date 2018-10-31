package com.kkisiele.application;

import com.kkisiele.infrastructure.PayrollDatabase;

public class DeleteEmployeeTransaction implements Transaction {
    private final int id;

    public DeleteEmployeeTransaction(int id) {
        this.id = id;
    }

    @Override
    public void execute() {
        PayrollDatabase.deleteEmployee(id);
    }
}
