package com.kkisiele.domain;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CommissionClassification implements PaymentClassification {
    private final double baseSalary;
    private final double commissionRate;
    private final Map<LocalDate, SalesReceipt> salesReceipts = new HashMap<>();

    public CommissionClassification(double baseSalary, double commissionRate) {
        this.baseSalary = baseSalary;
        this.commissionRate = commissionRate;
    }

    public double baseSalary() {
        return baseSalary;
    }

    public double commissionRate() {
        return commissionRate;
    }

    @Override
    public double calculatePay(Paycheck paycheck) {
        double salesTotal = 0.0;
        for(SalesReceipt receipt : salesReceipts.values()) {
            if(DateUtil.isInPayPeriod(receipt.date(), paycheck.payStartDate(), paycheck.payDate())) {
                salesTotal += receipt.saleAmount();
            }
        }
        return baseSalary + (salesTotal * commissionRate * 0.01);
    }

    public void addSalesReceipt(SalesReceipt salesReceipt) {
        salesReceipts.put(salesReceipt.date(), salesReceipt);
    }
}
