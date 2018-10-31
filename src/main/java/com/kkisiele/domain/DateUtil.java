package com.kkisiele.domain;

import java.time.LocalDate;

public class DateUtil {
    public static boolean isInPayPeriod(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return (date.compareTo(startDate) >= 0) && (date.compareTo(endDate) <= 0);
    }
}
