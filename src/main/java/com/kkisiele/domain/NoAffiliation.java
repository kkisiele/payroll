package com.kkisiele.domain;

public class NoAffiliation implements Affiliation {
    @Override
    public double calculateDeductions(Paycheck paycheck) {
        return 0;
    }
}
