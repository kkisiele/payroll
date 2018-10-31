package com.kkisiele.application;

import com.kkisiele.domain.BiWeeklySchedule;
import com.kkisiele.domain.CommissionClassification;
import com.kkisiele.domain.PaymentClassification;
import com.kkisiele.domain.PaymentSchedule;

public class AddCommissionedEmployee extends AddEmployeeTransaction {
    private final double baseSalary;
    private final double commissionRate;

    public AddCommissionedEmployee(int empId, String name, String address, double baseSalary, double commissionRate) {
        super(empId, name, address);
        this.baseSalary = baseSalary;
        this.commissionRate = commissionRate;
    }

    @Override
    protected PaymentClassification makeClassification() {
        return new CommissionClassification(baseSalary, commissionRate);
    }

    @Override
    protected PaymentSchedule makeSchedule() {
        return new BiWeeklySchedule();
    }
}
