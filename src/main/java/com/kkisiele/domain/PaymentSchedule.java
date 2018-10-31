package com.kkisiele.domain;

import java.time.LocalDate;

public interface PaymentSchedule {
    boolean isPayDate(LocalDate payDate);

    LocalDate getPayPeriodStartDate(LocalDate date);
}
