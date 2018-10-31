package com.kkisiele.domain;

import java.time.LocalDate;

public class TimeCard {
    private final LocalDate date;
    private final double hours;

    public TimeCard(LocalDate date, double hours) {
        this.date = date;
        this.hours = hours;
    }

    public LocalDate date() {
        return date;
    }

    public double hours() {
        return hours;
    }
}
