package com.kkisiele.domain;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class HourlyClassification implements PaymentClassification {
    private final double hourRate;
    private final Map<LocalDate, TimeCard> timeCards = new HashMap<>();

    public HourlyClassification(double hourRate) {
        this.hourRate = hourRate;
    }

    public double hourRate() {
        return hourRate;
    }

    public TimeCard getTimeCard(LocalDate date) {
        return timeCards.get(date);
    }

    public void addTimeCard(TimeCard timeCard) {
        timeCards.put(timeCard.date(), timeCard);
    }

    @Override
    public double calculatePay(Paycheck paycheck) {
        double totalPay = 0.0;
        for(TimeCard timeCard : timeCards.values()) {
            if(DateUtil.isInPayPeriod(timeCard.date(), paycheck.payStartDate(), paycheck.payDate())) {
                totalPay += calculatePayForTimeCard(timeCard);
            }
        }
        return totalPay;
    }

    private boolean isInPayPeriod(TimeCard timeCard, LocalDate payDate) {
        LocalDate endDate = payDate;
        LocalDate startDate = payDate.minusDays(5);
        return timeCard.date().compareTo(endDate) <= 0 && timeCard.date().compareTo(startDate) >= 0;
    }

    private double calculatePayForTimeCard(TimeCard timeCard) {
        double overtimeHours = Math.max(0.0, timeCard.hours() - 8);
        double normalHours = timeCard.hours() - overtimeHours;
        return hourRate * normalHours + hourRate * 1.5 * overtimeHours;
    }
}
