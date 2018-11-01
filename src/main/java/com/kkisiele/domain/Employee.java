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

    public Employee(int empId, String name, String address) {
        this.empId = empId;
        this.name = name;
        this.address = address;
    }

    public String name() {
        return name;
    }

    public void setClassification(PaymentClassification classification) {
        this.classification = classification;
    }

    public void setSchedule(PaymentSchedule schedule) {
        this.schedule = schedule;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
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

    public void addTimeCard(TimeCard timeCard) {
        if(classification instanceof HourlyClassification) {
            HourlyClassification hc = (HourlyClassification) classification;
            hc.addTimeCard(timeCard);
            return;
        }

        throw new RuntimeException("Tried to add time card to non-hourly employee");
    }

    public void addServiceCharge(ServiceCharge charge) {
        if(affiliation instanceof UnionAffiliation) {
            UnionAffiliation ua = (UnionAffiliation) affiliation;
            ua.addServiceCharge(charge);
            return;
        }

        throw new RuntimeException("Tries to add service charge to union member without a union affiliation");
    }
}
