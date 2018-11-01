package com.kkisiele.application;

import com.kkisiele.domain.*;
import com.kkisiele.infrastructure.InMemoryDatabase;

public class ChangeCommissionedTransaction implements Transaction {
    private final int empId;
    private final double baseSalary;
    private final double commissionRate;

    public ChangeCommissionedTransaction(int empId, double baseSalary, double commissionRate) {
        this.empId = empId;
        this.baseSalary = baseSalary;
        this.commissionRate = commissionRate;
    }

    @Override
    public void execute() {
        Employee employee = InMemoryDatabase.getEmployee(empId);
        if(employee != null) {
            employee.setClassification(new CommissionClassification(baseSalary, commissionRate));
            employee.setSchedule(new BiWeeklySchedule());
        }
    }
}
