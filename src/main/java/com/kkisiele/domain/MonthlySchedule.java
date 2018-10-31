package com.kkisiele.domain;

import java.time.LocalDate;
import java.time.Month;

public class MonthlySchedule implements PaymentSchedule {
    @Override
    public boolean isPayDate(LocalDate payDate) {
        return isLastDayOfMonth(payDate);
    }

    @Override
    public LocalDate getPayPeriodStartDate(LocalDate date) {
        return date.withDayOfMonth(1);
    }

    private boolean isLastDayOfMonth(LocalDate date) {
        Month m1 = date.getMonth();
        Month m2 = date.plusDays(1).getMonth();
        return !m1.equals(m2);
    }
}
