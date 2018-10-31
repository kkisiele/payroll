package com.kkisiele.application;

import com.kkisiele.domain.BiWeeklySchedule;
import com.kkisiele.domain.CommissionClassification;
import com.kkisiele.domain.PaymentClassification;
import com.kkisiele.domain.PaymentSchedule;

public class ChangeCommissionedTransaction extends ChangeClassificationTransaction {
    private final double baseSalary;
    private final double commissionRate;

    public ChangeCommissionedTransaction(int empId, double baseSalary, double commissionRate) {
        super(empId);
        this.baseSalary = baseSalary;
        this.commissionRate = commissionRate;
    }

    @Override
    protected PaymentClassification getClassification() {
        return new CommissionClassification(baseSalary, commissionRate);
    }

    @Override
    protected PaymentSchedule getSchedule() {
        return new BiWeeklySchedule();
    }
}
