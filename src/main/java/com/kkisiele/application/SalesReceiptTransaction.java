package com.kkisiele.application;

import com.kkisiele.domain.CommissionClassification;
import com.kkisiele.domain.Employee;
import com.kkisiele.domain.SalesReceipt;
import com.kkisiele.infrastructure.PayrollDatabase;

import java.time.LocalDate;

public class SalesReceiptTransaction implements Transaction {
    private final LocalDate payDate;
    private final double saleAmount;
    private final int empId;

    public SalesReceiptTransaction(LocalDate payDate, double saleAmount, int empId) {
        this.payDate = payDate;
        this.saleAmount = saleAmount;
        this.empId = empId;
    }

    @Override
    public void execute() {
        Employee e = PayrollDatabase.getEmployee(empId);
        if(e != null) {
             if(e.classification() instanceof CommissionClassification) {
                 CommissionClassification cc = (CommissionClassification) e.classification();
                 cc.addSalesReceipt(new SalesReceipt(payDate, saleAmount));
             }
        }
    }
}
