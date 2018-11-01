package com.kkisiele.application;

import com.kkisiele.domain.Employee;
import com.kkisiele.domain.ServiceCharge;
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
        e.addServiceCharge(new ServiceCharge(date, charge));
    }
}
