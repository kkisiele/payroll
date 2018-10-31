package com.kkisiele.domain;

import java.time.LocalDate;

public class ServiceCharge {
    private final LocalDate date;
    private final double amount;

    public ServiceCharge(LocalDate date, double amount) {
        this.date = date;
        this.amount = amount;
    }

    public LocalDate date() {
        return date;
    }

    public double amount() {
        return amount;
    }
}
