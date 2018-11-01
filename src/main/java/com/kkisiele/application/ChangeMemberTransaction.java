package com.kkisiele.application;

import com.kkisiele.domain.Employee;
import com.kkisiele.domain.UnionAffiliation;
import com.kkisiele.infrastructure.InMemoryDatabase;

public class ChangeMemberTransaction implements Transaction {
    private final int empId;
    private final int memberId;
    private final double dues;

    public ChangeMemberTransaction(int empId, int memberId, double dues) {
        this.empId = empId;
        this.memberId = memberId;
        this.dues = dues;
    }

    @Override
    public void execute() {
        Employee employee = InMemoryDatabase.getEmployee(empId);
        InMemoryDatabase.addUnionMember(memberId, employee);
        employee.setAffiliation(new UnionAffiliation(memberId, dues));
    }
}
