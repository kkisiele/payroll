package com.kkisiele.application;

import com.kkisiele.domain.Affiliation;
import com.kkisiele.domain.Employee;
import com.kkisiele.domain.NoAffiliation;
import com.kkisiele.domain.UnionAffiliation;
import com.kkisiele.infrastructure.PayrollDatabase;

public class ChangeUnaffiliatedTransaction extends ChangeAffiliationTransaction {
    public ChangeUnaffiliatedTransaction(int empId) {
        super(empId);
    }

    @Override
    protected void recordMembership(Employee e) {
        if(e.affiliation() instanceof UnionAffiliation) {
            UnionAffiliation ua = (UnionAffiliation) e.affiliation();
            PayrollDatabase.removeUnionMember(ua.memberId());
        }
    }

    @Override
    protected Affiliation getAffiliation() {
        return new NoAffiliation();
    }
}
