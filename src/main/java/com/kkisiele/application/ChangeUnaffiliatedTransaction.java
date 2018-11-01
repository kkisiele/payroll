package com.kkisiele.application;

import com.kkisiele.domain.Employee;
import com.kkisiele.domain.NoAffiliation;
import com.kkisiele.domain.UnionAffiliation;
import com.kkisiele.infrastructure.InMemoryDatabase;

public class ChangeUnaffiliatedTransaction implements Transaction {
    private final int empId;

    public ChangeUnaffiliatedTransaction(int empId) {
        this.empId = empId;
    }

    @Override
    public void execute() {
        Employee employee = InMemoryDatabase.getEmployee(empId);
        if(employee.affiliation() instanceof UnionAffiliation) {
            UnionAffiliation ua = (UnionAffiliation) employee.affiliation();
            InMemoryDatabase.removeUnionMember(ua.memberId());
        }
        employee.setAffiliation(new NoAffiliation());
    }
}
