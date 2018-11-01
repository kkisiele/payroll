package com.kkisiele.domain;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Paycheck {
    private final LocalDate payStartDate;
    private final LocalDate payDate;
    private double grossPay;
    private double netPay;
    private double deductions;
    private final Map<String, String> fields = new HashMap<>();

    public Paycheck(LocalDate payStartDate, LocalDate payDate) {
        this.payStartDate = payStartDate;
        this.payDate = payDate;
    }

    public LocalDate payStartDate() {
        return payStartDate;
    }

    public LocalDate payDate() {
        return payDate;
    }

    public double grossPay() {
        return grossPay;
    }

    public String getField(String fieldName) {
        return fields.get(fieldName);
    }

    public double deductions() {
        return deductions;
    }

    public double netPay() {
        return netPay;
    }

    public void setGrossPay(double grossPay) {
        this.grossPay = grossPay;
    }

    public void setDeductions(double deductions) {
        this.deductions = deductions;
    }

    public void setNetPay(double netPay) {
        this.netPay = netPay;
    }

    public void setField(String fieldName, String value) {
        fields.put(fieldName, value);
    }

    public boolean isInPayPeriod(LocalDate date) {
        return (date.compareTo(payStartDate) >= 0) && (date.compareTo(payDate) <= 0);
    }
}
