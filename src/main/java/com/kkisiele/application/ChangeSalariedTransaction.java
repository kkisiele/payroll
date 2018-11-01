package com.kkisiele.application;

import com.kkisiele.domain.*;
import com.kkisiele.infrastructure.InMemoryDatabase;

public class ChangeSalariedTransaction implements Transaction {
    private final int empId;
    private final double salary;

    public ChangeSalariedTransaction(int empId, double salary) {
        this.empId = empId;
        this.salary = salary;
    }
    @Override
    public void execute() {
        Employee employee = InMemoryDatabase.getEmployee(empId);
        if(employee != null) {
            employee.setClassification(new SalariedClassification(salary));
            employee.setSchedule(new MonthlySchedule());
        }
    }

}
