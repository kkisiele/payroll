package com.kkisiele.application;

import com.kkisiele.domain.Employee;
import com.kkisiele.domain.TimeCard;
import com.kkisiele.infrastructure.InMemoryDatabase;

import java.time.LocalDate;

public class TimeCardTransaction implements Transaction {
    private final LocalDate date;
    private final double hours;
    private final int empId;

    public TimeCardTransaction(LocalDate date, double hours, int empId) {
        this.date = date;
        this.hours = hours;
        this.empId = empId;
    }

    @Override
    public void execute() {
        Employee employee = InMemoryDatabase.getEmployee(empId);
        employee.addTimeCard(new TimeCard(date, hours));
    }
}
