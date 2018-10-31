package com.kkisiele.application;

import com.kkisiele.domain.Employee;
import com.kkisiele.domain.PaymentClassification;
import com.kkisiele.domain.PaymentSchedule;

public abstract class ChangeClassificationTransaction extends ChangeEmployeeTransaction {
    public ChangeClassificationTransaction(int empId) {
        super(empId);
    }

    @Override
    protected void change(Employee e) {
        e.setClassification(getClassification());
        e.setSchedule(getSchedule());
    }

    protected abstract PaymentClassification getClassification();
    protected abstract PaymentSchedule getSchedule();
}
