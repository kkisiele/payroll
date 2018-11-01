package com.kkisiele.application;

import com.kkisiele.infrastructure.InMemoryDatabase;

public class DeleteEmployeeTransaction implements Transaction {
    private final int id;

    public DeleteEmployeeTransaction(int id) {
        this.id = id;
    }

    @Override
    public void execute() {
        InMemoryDatabase.deleteEmployee(id);
    }
}
