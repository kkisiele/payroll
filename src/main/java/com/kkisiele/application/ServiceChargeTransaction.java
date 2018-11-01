package com.kkisiele.application;

import com.kkisiele.domain.Employee;
import com.kkisiele.domain.ServiceCharge;
import com.kkisiele.domain.UnionAffiliation;
import com.kkisiele.infrastructure.InMemoryDatabase;

import java.time.LocalDate;

public class ServiceChargeTransaction implements Transaction {
    private final int memberId;
    private final LocalDate date;
    private final double charge;

    public ServiceChargeTransaction(int memberId, LocalDate date, double charge) {
        this.memberId = memberId;
        this.date = date;
        this.charge = charge;
    }

    @Override
    public void execute() {
        Employee e = InMemoryDatabase.getUnionMember(memberId);
        if(e != null) {
            UnionAffiliation ua = null;
            if(e.affiliation() instanceof UnionAffiliation) {
                ua = (UnionAffiliation) e.affiliation();
            }

            if(ua != null)
                ua.addServiceCharge(new ServiceCharge(date, charge));
            else
                throw new RuntimeException("Tries to add service charge to union member without a union affiliation");

        } else {
            throw new RuntimeException("No such union member");
        }
    }
}
