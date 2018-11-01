package com.kkisiele.application;

import com.kkisiele.domain.Employee;
import com.kkisiele.domain.Paycheck;
import com.kkisiele.infrastructure.InMemoryDatabase;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class PaydayTransaction implements Transaction {
    private final LocalDate payDate;
    private final Map<Integer, Paycheck> paychecks = new HashMap<>();

    public PaydayTransaction(LocalDate payDate) {
        this.payDate = payDate;
    }

    @Override
    public void execute() {
        for(Integer empId : InMemoryDatabase.getAllEmployeeIds()) {
            Employee employee = InMemoryDatabase.getEmployee(empId);
            if(employee.isPayDate(payDate)) {
                LocalDate startDate = employee.getPayPeriodStartDate(payDate);
                Paycheck pc = new Paycheck(startDate, payDate);
                paychecks.put(empId, pc);
                employee.payday(pc);
            }
        }
    }

    public Paycheck getPaycheck(int empId) {
        return paychecks.get(empId);
    }
}
