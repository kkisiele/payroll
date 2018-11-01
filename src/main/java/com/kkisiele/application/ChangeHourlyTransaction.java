package com.kkisiele.application;

import com.kkisiele.domain.*;
import com.kkisiele.infrastructure.InMemoryDatabase;

public class ChangeHourlyTransaction implements Transaction {
    private final int empId;
    private final double hourRate;

    public ChangeHourlyTransaction(int empId, double hourRate) {
        this.empId = empId;
        this.hourRate = hourRate;
    }

    @Override
    public void execute() {
        Employee employee = InMemoryDatabase.getEmployee(empId);
        if(employee != null) {
            employee.setClassification(new HourlyClassification(hourRate));
            employee.setSchedule(new WeeklySchedule());
        }
    }

}
