package com.kkisiele.application;

import com.kkisiele.domain.Affiliation;
import com.kkisiele.domain.Employee;
import com.kkisiele.domain.UnionAffiliation;
import com.kkisiele.infrastructure.PayrollDatabase;

public class ChangeMemberTransaction extends ChangeAffiliationTransaction {
    private final int memberId;
    private final double dues;

    public ChangeMemberTransaction(int empId, int memberId, double dues) {
        super(empId);
        this.memberId = memberId;
        this.dues = dues;
    }

    @Override
    protected void recordMembership(Employee e) {
        PayrollDatabase.addUnionMember(memberId, e);
    }

    @Override
    protected Affiliation getAffiliation() {
        return new UnionAffiliation(memberId, dues);
    }
}
