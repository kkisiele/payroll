package com.kkisiele.domain;

import java.time.LocalDate;

public class SalesReceipt {
    private final LocalDate date;
    private final double saleAmount;

    public SalesReceipt(LocalDate date, double saleAmount) {
        this.date = date;
        this.saleAmount = saleAmount;
    }

    public LocalDate date() {
        return date;
    }

    public double saleAmount() {
        return saleAmount;
    }
}
