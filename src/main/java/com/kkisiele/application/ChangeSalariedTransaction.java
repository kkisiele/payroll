package com.kkisiele.application;

import com.kkisiele.domain.MonthlySchedule;
import com.kkisiele.domain.PaymentClassification;
import com.kkisiele.domain.PaymentSchedule;
import com.kkisiele.domain.SalariedClassification;

public class ChangeSalariedTransaction extends ChangeClassificationTransaction {
    private final double salary;

    public ChangeSalariedTransaction(int empId, double salary) {
        super(empId);
        this.salary = salary;
    }

    @Override
    protected PaymentClassification getClassification() {
        return new SalariedClassification(salary);
    }

    @Override
    protected PaymentSchedule getSchedule() {
        return new MonthlySchedule();
    }
}
