package com.kkisiele.domain;

public class DirectDepositMethod implements PaymentMethod {
    private final String bank;
    private final String accountNumber;

    public DirectDepositMethod(String bank, String accountNumber) {
        this.bank = bank;
        this.accountNumber = accountNumber;
    }

    @Override
    public void pay(Paycheck paycheck) {
        paycheck.setField("Disposition", "Direct");
    }

    public String bank() {
        return bank;
    }

    public String accountNumber() {
        return accountNumber;
    }
}
