package com.kkisiele.application;

import com.kkisiele.domain.HourlyClassification;
import com.kkisiele.domain.PaymentClassification;
import com.kkisiele.domain.PaymentSchedule;
import com.kkisiele.domain.WeeklySchedule;

public class ChangeHourlyTransaction extends ChangeClassificationTransaction {
    private final double hourRate;

    public ChangeHourlyTransaction(int empId, double hourRate) {
        super(empId);
        this.hourRate = hourRate;
    }

    @Override
    protected PaymentClassification getClassification() {
        return new HourlyClassification(hourRate);
    }

    @Override
    protected PaymentSchedule getSchedule() {
        return new WeeklySchedule();
    }
}
