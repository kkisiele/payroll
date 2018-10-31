package com.kkisiele.application;

import com.kkisiele.domain.MailMethod;
import com.kkisiele.domain.PaymentMethod;

public class ChangeMailTransaction extends ChangeMethodTransaction {
    public ChangeMailTransaction(int empId) {
        super(empId);
    }

    @Override
    protected PaymentMethod getMethod() {
        return new MailMethod("1 Infinite Loop");
    }
}
