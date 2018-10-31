package com.kkisiele.application;

import com.kkisiele.domain.MonthlySchedule;
import com.kkisiele.domain.PaymentClassification;
import com.kkisiele.domain.PaymentSchedule;
import com.kkisiele.domain.SalariedClassification;

public class AddSalariedEmployee extends AddEmployeeTransaction {
    private final double salary;

    public AddSalariedEmployee(int empId, String name, String address, double salary) {
        super(empId, name, address);
        this.salary = salary;
    }

    @Override
    protected PaymentClassification makeClassification() {
        return new SalariedClassification(salary);
    }

    @Override
    protected PaymentSchedule makeSchedule() {
        return new MonthlySchedule();
    }
}
