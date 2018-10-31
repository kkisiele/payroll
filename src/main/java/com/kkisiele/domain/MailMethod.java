package com.kkisiele.domain;

public class MailMethod implements PaymentMethod {
    private final String address;

    public MailMethod(String address) {
        this.address = address;
    }

    @Override
    public void pay(Paycheck paycheck) {
        paycheck.setField("Disposition", "Mail");
    }

    public String address() {
        return address;
    }
}
