package com.kkisiele.application;

import com.kkisiele.domain.HourlyClassification;
import com.kkisiele.domain.PaymentClassification;
import com.kkisiele.domain.PaymentSchedule;
import com.kkisiele.domain.WeeklySchedule;

public class AddHourlyEmployee extends AddEmployeeTransaction {
    private final double hourRate;

    public AddHourlyEmployee(int empId, String name, String address, double hourRate) {
        super(empId, name, address);
        this.hourRate = hourRate;
    }

    @Override
    protected PaymentClassification makeClassification() {
        return new HourlyClassification(hourRate);
    }

    @Override
    protected PaymentSchedule makeSchedule() {
        return new WeeklySchedule();
    }
}
