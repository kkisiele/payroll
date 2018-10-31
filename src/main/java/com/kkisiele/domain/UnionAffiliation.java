package com.kkisiele.domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class UnionAffiliation implements Affiliation {
    private final Map<LocalDate, ServiceCharge> charges = new HashMap<>();
    private final int memberId;
    private final double dues;

    public UnionAffiliation() {
        this(-1, 0.0);
    }

    public UnionAffiliation(int memberId, double dues) {
        this.memberId = memberId;
        this.dues = dues;
    }

    public ServiceCharge getServiceCharge(LocalDate date) {
        return charges.get(date);
    }

    public void addServiceCharge(ServiceCharge serviceCharge) {
        charges.put(serviceCharge.date(), serviceCharge);
    }

    public double dues() {
        return dues;
    }

    public int memberId() {
        return memberId;
    }

    @Override
    public double calculateDeductions(Paycheck paycheck) {
        double totalDues = 0;
        int fridays = numberOfFridaysInPayPeriod(paycheck.payStartDate(), paycheck.payDate());
        totalDues = dues * fridays;

        for(ServiceCharge charge : charges.values()) {
            if(DateUtil.isInPayPeriod(charge.date(), paycheck.payStartDate(), paycheck.payDate())) {
                totalDues += charge.amount();
            }
        }
        return totalDues;
    }

    private int numberOfFridaysInPayPeriod(LocalDate startDate, LocalDate endDate) {
        int fridays = 0;
        for(LocalDate day = startDate; day.compareTo(endDate) <= 0; day = day.plusDays(1)) {
            if(day.getDayOfWeek() == DayOfWeek.FRIDAY) {
                fridays++;
            }
        }
        return fridays;
    }
}
