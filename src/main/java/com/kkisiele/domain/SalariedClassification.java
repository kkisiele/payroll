package com.kkisiele.domain;

public class SalariedClassification implements PaymentClassification {
    private final double salary;

    public SalariedClassification(double salary) {
        this.salary = salary;
    }

    public double salary() {
        return salary;
    }

    @Override
    public double calculatePay(Paycheck paycheck) {
        return salary;
    }
}
