package com.kkisiele.application;

import com.kkisiele.domain.DirectDepositMethod;
import com.kkisiele.domain.PaymentMethod;

public class ChangeDirectTransaction extends ChangeMethodTransaction {
    public ChangeDirectTransaction(int empId) {
        super(empId);
    }

    @Override
    protected PaymentMethod getMethod() {
        return new DirectDepositMethod("mBank", "123");
    }
}
