package com.kkisiele.domain;

import java.time.LocalDate;

public class Employee {
    private final int empId;
    private String name;
    private final String address;
    private PaymentClassification classification;
    private PaymentSchedule schedule;
    private PaymentMethod method;
    private Affiliation affiliation = new NoAffiliation();

    public Employee(int empId, String name, String address, PaymentClassification classification, PaymentSchedule schedule, PaymentMethod method) {
        this.empId = empId;
        this.name = name;
        this.address = address;
        this.classification = classification;
        this.schedule = schedule;
        this.method = method;
    }

    public String name() {
        return name;
    }

    public void changeClassification(PaymentClassification newClassification) {
        this.classification = newClassification;
    }

    public void changeSchedule(PaymentSchedule newSchedule) {
        this.schedule = newSchedule;
    }

    public void changeMethod(PaymentMethod newMethod) {
        this.method = newMethod;
    }

    public PaymentClassification classification() {
        return classification;
    }

    public PaymentSchedule schedule() {
        return schedule;
    }

    public PaymentMethod method() {
        return method;
    }

    public Affiliation affiliation() {
        return affiliation;
    }

    public void setAffiliation(Affiliation affiliation) {
        this.affiliation = affiliation;
    }

    public void setName(String newName) {
        name = newName;
    }

    public boolean isPayDate(LocalDate payDate) {
        return schedule.isPayDate(payDate);
    }

    public void payday(Paycheck paycheck) {
        double grossPay = classification.calculatePay(paycheck);
        double deductions = affiliation.calculateDeductions(paycheck);
        paycheck.setGrossPay(grossPay);
        paycheck.setDeductions(deductions);
        paycheck.setNetPay(grossPay-deductions);
        method.pay(paycheck);
    }

    public LocalDate getPayPeriodStartDate(LocalDate date) {
        return schedule.getPayPeriodStartDate(date);
    }

    public void registerTime(LocalDate date, double hours) {
        if(classification instanceof HourlyClassification) {
            HourlyClassification hc = (HourlyClassification) classification;
            hc.addTimeCard(new TimeCard(date, hours));
            return;
        }

        throw new RuntimeException("Tried to register time to non-hourly employee");
    }

    public void registerCharge(LocalDate date, double charge) {
        if(affiliation instanceof UnionAffiliation) {
            UnionAffiliation ua = (UnionAffiliation) affiliation;
            ua.addServiceCharge(new ServiceCharge(date, charge));
            return;
        }

        throw new RuntimeException("Tries to register service charge to union member without a union affiliation");
    }

    public void registerSale(LocalDate date, double saleAmount) {
        if(classification instanceof CommissionClassification) {
            CommissionClassification cc = (CommissionClassification) classification;
            cc.addSalesReceipt(new SalesReceipt(date, saleAmount));
            return;
        }

        throw new RuntimeException("Tries to register sale to non-commission employee");
    }
}
