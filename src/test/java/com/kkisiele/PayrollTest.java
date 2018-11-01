package com.kkisiele;

import com.kkisiele.domain.*;
import com.kkisiele.infrastructure.InMemoryDatabase;
import org.junit.Assert;
import org.junit.Test;
import com.kkisiele.application.*;

import java.time.LocalDate;

public class PayrollTest {
    @Test
    public void addSalariedEmployee() {
        int empId = 1;
        AddSalariedEmployee t = new AddSalariedEmployee(empId, "Bob", "Home", 1000.00);
        t.execute();

        Employee e = InMemoryDatabase.getEmployee(empId);
        Assert.assertEquals("Bob", e.name());

        PaymentClassification pc = e.classification();
        Assert.assertTrue(SalariedClassification.class.isAssignableFrom(pc.getClass()));
        SalariedClassification sc = (SalariedClassification)pc;
        Assert.assertEquals(1000.00, sc.salary(), 0.001);
        PaymentSchedule ps = e.schedule();
        Assert.assertTrue(MonthlySchedule.class.isAssignableFrom(ps.getClass()));
        PaymentMethod pm = e.method();
        Assert.assertTrue(HoldMethod.class.isAssignableFrom(pm.getClass()));
    }

    @Test
    public void addHourlyEmployee() {
        int empId = 1;
        AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bob", "Home", 10.00);
        t.execute();

        Employee e = InMemoryDatabase.getEmployee(empId);
        Assert.assertEquals("Bob", e.name());

        PaymentClassification pc = e.classification();
        Assert.assertTrue(HourlyClassification.class.isAssignableFrom(pc.getClass()));
        HourlyClassification hc = (HourlyClassification)pc;
        Assert.assertEquals(10.00, hc.hourRate(), 0.001);
        PaymentSchedule ps = e.schedule();
        Assert.assertTrue(WeeklySchedule.class.isAssignableFrom(ps.getClass()));
        PaymentMethod pm = e.method();
        Assert.assertTrue(HoldMethod.class.isAssignableFrom(pm.getClass()));
    }

    @Test
    public void addCommissionedEmployee() {
        int empId = 1;
        AddCommissionedEmployee t = new AddCommissionedEmployee(empId, "Bob", "Home", 1200, 20);
        t.execute();

        Employee e = InMemoryDatabase.getEmployee(empId);
        Assert.assertEquals("Bob", e.name());

        PaymentClassification pc = e.classification();
        Assert.assertTrue(CommissionClassification.class.isAssignableFrom(pc.getClass()));
        CommissionClassification cc = (CommissionClassification)pc;
        Assert.assertEquals(1200.00, cc.baseSalary(), 0.001);
        Assert.assertEquals(20.00, cc.commissionRate(), 0.001);
        PaymentSchedule ps = e.schedule();
        Assert.assertTrue(BiWeeklySchedule.class.isAssignableFrom(ps.getClass()));
        PaymentMethod pm = e.method();
        Assert.assertTrue(HoldMethod.class.isAssignableFrom(pm.getClass()));
    }

    @Test
    public void deleteEmployee() {
        int empId = 4;
        AddCommissionedEmployee t = new AddCommissionedEmployee(empId, "Bill", "Home", 2500, 3);
        t.execute();

        Employee e = InMemoryDatabase.getEmployee(empId);
        Assert.assertNotNull(e);
        DeleteEmployeeTransaction dt = new DeleteEmployeeTransaction(empId);
        dt.execute();
        e = InMemoryDatabase.getEmployee(empId);
        Assert.assertNull(e);
    }

    @Test
    public void timeCardTransaction() {
        int empId = 5;
        AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bill", "Home", 15.25);
        t.execute();
        TimeCardTransaction tct = new TimeCardTransaction(LocalDate.of(2005, 7, 31), 8.0, empId);
        tct.execute();

        Employee e = InMemoryDatabase.getEmployee(empId);
        Assert.assertNotNull(e);

        PaymentClassification pc = e.classification();
        Assert.assertTrue(HourlyClassification.class.isAssignableFrom(pc.getClass()));
        HourlyClassification hc = (HourlyClassification) pc;
        TimeCard tc = hc.getTimeCard(LocalDate.of(2005, 7, 31));
        Assert.assertNotNull(tc);
        Assert.assertEquals(8.0, tc.hours(), 0.001);
    }

    @Test
    public void addServiceCharge() {
        int empId = 2;
        AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bill", "Home", 15.25);
        t.execute();
        Employee e = InMemoryDatabase.getEmployee(empId);
        Assert.assertNotNull(e);

        UnionAffiliation af = new UnionAffiliation();
        e.setAffiliation(af);
        int memberId = 86; // Maxwell Smart
        InMemoryDatabase.addUnionMember(memberId, e);
        ServiceChargeTransaction sct = new ServiceChargeTransaction(memberId, LocalDate.of(2005, 8, 8), 12.95);
        sct.execute();
        ServiceCharge sc = af.getServiceCharge(LocalDate.of(2005, 8, 8));
        Assert.assertNotNull(sc);
        Assert.assertEquals(12.95, sc.amount(), 0.001);
    }

    @Test
    public void changeNameTransaction() {
        int empId = 2;
        AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bill", "Home", 15.25);
        t.execute();

        ChangeNameTransaction cnt = new ChangeNameTransaction(empId, "Bob");
        cnt.execute();
        Employee e = InMemoryDatabase.getEmployee(empId);
        Assert.assertNotNull(e);
        Assert.assertEquals("Bob", e.name());
    }

    @Test
    public void changeHourlyTransaction() {
        int empId = 3;
        AddCommissionedEmployee t = new AddCommissionedEmployee(empId, "Lance", "Home", 2500, 3.2);
        t.execute();

        ChangeHourlyTransaction cht = new ChangeHourlyTransaction(empId, 27.52);
        cht.execute();
        Employee e = InMemoryDatabase.getEmployee(empId);
        Assert.assertNotNull(e);
        PaymentClassification pc = e.classification();
        Assert.assertNotNull(pc);
        Assert.assertTrue(pc instanceof HourlyClassification);
        HourlyClassification hc = (HourlyClassification)pc;
        Assert.assertEquals(27.52, hc.hourRate(), .001);
        PaymentSchedule ps = e.schedule();
        Assert.assertTrue(ps instanceof WeeklySchedule);
    }

    @Test
    public void changeSalaryTransaction() {
        int empId = 4;
        AddCommissionedEmployee t = new AddCommissionedEmployee(empId, "Lance", "Home", 2500, 3.2);
        t.execute();
        ChangeSalariedTransaction cst = new ChangeSalariedTransaction(empId, 3000.00);
        cst.execute();
        Employee e = InMemoryDatabase.getEmployee(empId);
        Assert.assertNotNull(e);
        PaymentClassification pc = e.classification();
        Assert.assertNotNull(pc);
        Assert.assertTrue(pc instanceof SalariedClassification);
        SalariedClassification sc = (SalariedClassification)pc;
        Assert.assertEquals(3000.00, sc.salary(), .001);
        PaymentSchedule ps = e.schedule();
        Assert.assertTrue(ps instanceof MonthlySchedule);
    }

    @Test
    public void changeCommissionTransaction() {
        int empId = 5;
        AddSalariedEmployee t = new AddSalariedEmployee(empId, "Bob", "Home", 2500.00);
        t.execute();
        ChangeCommissionedTransaction cht = new ChangeCommissionedTransaction(empId, 1250.00, 5.6);
        cht.execute();
        Employee e = InMemoryDatabase.getEmployee(empId);
        Assert.assertNotNull(e);
        PaymentClassification pc = e.classification();
        Assert.assertNotNull(pc);
        Assert.assertTrue(pc instanceof CommissionClassification);
        CommissionClassification cc = (CommissionClassification)pc;
        Assert.assertEquals(1250.00, cc.baseSalary(), .001);
        Assert.assertEquals(5.6, cc.commissionRate(), .001);
        PaymentSchedule ps = e.schedule();
        Assert.assertTrue(ps instanceof BiWeeklySchedule);
    }

    @Test
    public void changeDirectMethod() {
        int empId = 6;
        AddSalariedEmployee t = new AddSalariedEmployee(empId, "Mike", "Home", 3500.00);
        t.execute();
        ChangeDirectTransaction cddt = new ChangeDirectTransaction(empId);
        cddt.execute();
        Employee e = InMemoryDatabase.getEmployee(empId);
        Assert.assertNotNull(e);
        PaymentMethod method = e.method();
        Assert.assertNotNull(method);
        Assert.assertTrue(method instanceof DirectDepositMethod);
    }

    @Test
    public void changeHoldMethod() {
        int empId = 7;
        AddSalariedEmployee t = new AddSalariedEmployee(empId, "Mike", "Home", 3500.00);
        t.execute();
        new ChangeDirectTransaction(empId).execute();
        ChangeHoldTransaction cht = new ChangeHoldTransaction(empId);
        cht.execute();
        Employee e = InMemoryDatabase.getEmployee(empId);
        Assert.assertNotNull(e);
        PaymentMethod method = e.method();
        Assert.assertNotNull(method);
        Assert.assertTrue(method instanceof HoldMethod);
    }

    @Test
    public void changeMailMethod() {
        int empId = 8;
        AddSalariedEmployee t = new AddSalariedEmployee(empId, "Mike", "Home", 3500.00);
        t.execute();
        ChangeMailTransaction cmt = new ChangeMailTransaction(empId);
        cmt.execute();
        Employee e = InMemoryDatabase.getEmployee(empId);
        Assert.assertNotNull(e);
        PaymentMethod method = e.method();
        Assert.assertNotNull(method);
        Assert.assertTrue(method instanceof MailMethod);
    }

    @Test
    public void ChangeUnionMember() {
        int empId = 8;
        AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bill", "Home", 15.25);
        t.execute();
        int memberId = 7743;
        ChangeMemberTransaction cmt = new ChangeMemberTransaction(empId, memberId, 99.42);
        cmt.execute();
        Employee e = InMemoryDatabase.getEmployee(empId);
        Assert.assertNotNull(e);
        Affiliation affiliation = e.affiliation();
        Assert.assertNotNull(affiliation);
        Assert.assertTrue(affiliation instanceof UnionAffiliation);
        UnionAffiliation uf = (UnionAffiliation)affiliation;
        Assert.assertEquals(99.42, uf.dues(), .001);
        Employee member = InMemoryDatabase.getUnionMember(memberId);
        Assert.assertNotNull(member);
        Assert.assertEquals(e, member);
    }

    @Test
    public void ChangeUnaffiliatedMember() {
        int empId = 10;
        AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bill", "Home", 15.25);
        t.execute();

        int memberId = 7743;
        new ChangeMemberTransaction(empId, memberId, 99.42).execute();
        ChangeUnaffiliatedTransaction cut = new ChangeUnaffiliatedTransaction(empId);
        cut.execute();
        Employee e = InMemoryDatabase.getEmployee(empId);
        Assert.assertNotNull(e);
        Affiliation affiliation = e.affiliation();
        Assert.assertNotNull(affiliation);
        Assert.assertTrue(affiliation instanceof NoAffiliation);
        Employee member = InMemoryDatabase.getUnionMember(memberId);
        Assert.assertNull(member);
    }

    @Test
    public void paySingleSalariedEmployee() {
        int empId = 1;
        AddSalariedEmployee t = new AddSalariedEmployee(empId, "Bob", "Home", 1000.00);
        t.execute();

        LocalDate payDate = LocalDate.of(2001, 11, 30);
        PaydayTransaction pt = new PaydayTransaction(payDate);
        pt.execute();
        Paycheck pc = pt.getPaycheck(empId);
        Assert.assertNotNull(pc);
        Assert.assertEquals(payDate, pc.payDate());
        Assert.assertEquals(1000.00, pc.grossPay(), .001);
        Assert.assertEquals("Hold", pc.getField("Disposition"));
        Assert.assertEquals(0.0, pc.deductions(), .001);
        Assert.assertEquals(1000.00, pc.netPay(), .001);
    }

    @Test
    public void payingSingleHourlyEmployeeNoTimeCards() {
        int empId = 2;
        AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bill", "Home", 15.25);
        t.execute();

        LocalDate payDate = LocalDate.of(2001, 11, 9);
        PaydayTransaction pt = new PaydayTransaction(payDate);
        pt.execute();
        validatePaycheck(pt, empId, payDate, 0.0);
    }

    private void validatePaycheck(PaydayTransaction pt, int empid, LocalDate payDate, double pay) {
        Paycheck pc = pt.getPaycheck(empid);
        Assert.assertNotNull(pc);
        Assert.assertEquals(payDate, pc.payDate());
        Assert.assertEquals(pay, pc.grossPay(), .001);
        Assert.assertEquals("Hold", pc.getField("Disposition"));
        Assert.assertEquals(0.0, pc.deductions(), .001);
        Assert.assertEquals(pay, pc.netPay(), .001);
    }

    @Test
    public void paySingleHourlyEmployeeOneTimeCard() {
        int empId = 2;
        AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bill", "Home", 15.25);
        t.execute();

        LocalDate payDate = LocalDate.of(2001, 11, 9); // Friday
        TimeCardTransaction tc = new TimeCardTransaction(payDate, 2.0, empId);
        tc.execute();
        PaydayTransaction pt = new PaydayTransaction(payDate);
        pt.execute();
        validatePaycheck(pt, empId, payDate, 30.5);
    }

    @Test
    public void paySingleHourlyEmployeeOvertimeOneTimeCard() {
        int empId = 2;
        AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bill", "Home", 15.25);
        t.execute();

        LocalDate payDate = LocalDate.of(2001, 11, 9); // Friday
        TimeCardTransaction tc = new TimeCardTransaction(payDate, 9.0, empId);
        tc.execute();

        PaydayTransaction pt = new PaydayTransaction(payDate);
        pt.execute();

        validatePaycheck(pt, empId, payDate, (8 + 1.5)*15.25);
    }

    @Test
    public void paySingleHourlyEmployeeOnWrongDate() {
        int empId = 2;
        AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bill", "Home", 15.25);
        t.execute();

        LocalDate payDate = LocalDate.of(2001, 11, 8); // Thursday
        TimeCardTransaction tc = new TimeCardTransaction(payDate, 9.0, empId);
        tc.execute();
        PaydayTransaction pt = new PaydayTransaction(payDate);
        pt.execute();
        Paycheck pc = pt.getPaycheck(empId);
        Assert.assertNull(pc);
    }

    @Test
    public void paySingleHourlyEmployeeTwoTimeCards() {
        int empId = 2;
        AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bill", "Home", 15.25);
        t.execute();

        LocalDate payDate = LocalDate.of(2001, 11, 9); // Friday
        TimeCardTransaction tc = new TimeCardTransaction(payDate, 2.0, empId);
        tc.execute();
        TimeCardTransaction tc2 = new TimeCardTransaction(payDate.minusDays(1), 5.0,empId);
        tc2.execute();
        PaydayTransaction pt = new PaydayTransaction(payDate);
        pt.execute();
        validatePaycheck(pt, empId, payDate, 7*15.25);
    }

    @Test
    public void paySingleHourlyEmployeeWithTimeCardsSpanningTwoPayPeriods() {
        int empId = 2;
        AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bill", "Home", 15.25);
        t.execute();

        LocalDate payDate = LocalDate.of(2001, 11, 9); // Friday
        LocalDate dateInPreviousPayPeriod = LocalDate.of(2001, 11, 2);
        TimeCardTransaction tc = new TimeCardTransaction(payDate, 2.0, empId);
        tc.execute();
        TimeCardTransaction tc2 = new TimeCardTransaction(dateInPreviousPayPeriod, 5.0, empId);
        tc2.execute();
        PaydayTransaction pt = new PaydayTransaction(payDate);
        pt.execute();
        validatePaycheck(pt, empId, payDate, 2*15.25);
    }

    @Test
    public void payingSingleCommissionedEmployeeNoReceipts() {
        int empId = 2;
        AddCommissionedEmployee t = new AddCommissionedEmployee(empId, "Bill", "Home", 1500, 10);
        t.execute();

        LocalDate payDate = LocalDate.of(2001, 11, 16); // Payday
        PaydayTransaction pt = new PaydayTransaction(payDate);
        pt.execute();
        validatePaycheck(pt, empId, payDate, 1500.0);
    }

    @Test
    public void paySingleCommissionedEmployeeOneReceipt() {
        int empId = 2;
        AddCommissionedEmployee t = new AddCommissionedEmployee(empId, "Bill", "Home", 1500, 10);
        t.execute();
        LocalDate payDate = LocalDate.of(2001, 11, 16); // Payday

        SalesReceiptTransaction sr = new SalesReceiptTransaction(payDate, 5000.00, empId);
        sr.execute();
        PaydayTransaction pt = new PaydayTransaction(payDate);
        pt.execute();
        validatePaycheck(pt, empId, payDate, 2000.00);
    }

    @Test
    public void paySingleCommissionedEmployeeOnWrongDate() {
        int empId = 2;
        AddCommissionedEmployee t = new AddCommissionedEmployee(empId, "Bill", "Home", 1500, 10);
        t.execute();
        LocalDate payDate = LocalDate.of(2001, 11, 9); // wrong friday

        SalesReceiptTransaction sr = new SalesReceiptTransaction(payDate, 5000.00, empId);
        sr.execute();
        PaydayTransaction pt = new PaydayTransaction(payDate);
        pt.execute();

        Paycheck pc = pt.getPaycheck(empId);
        Assert.assertNotNull(pc);
    }

    @Test
    public void paySingleCommissionedEmployeeTwoReceipts() {
        int empId = 2;
        AddCommissionedEmployee t = new AddCommissionedEmployee(empId, "Bill", "Home", 1500, 10);
        t.execute();
        LocalDate payDate = LocalDate.of(2001, 11, 16); // Payday

        SalesReceiptTransaction sr = new SalesReceiptTransaction(payDate, 5000.00, empId);
        sr.execute();
        SalesReceiptTransaction sr2 = new SalesReceiptTransaction(payDate.minusDays(1), 3500.00, empId);
        sr2.execute();
        PaydayTransaction pt = new PaydayTransaction(payDate);
        pt.execute();
        validatePaycheck(pt, empId, payDate, 2350.00);
    }

    @Test
    public void testPaySingleCommissionedEmployeeWithReceiptsSpanningTwoPayPeriods() {
        int empId = 2;
        AddCommissionedEmployee t = new AddCommissionedEmployee(empId, "Bill", "Home", 1500, 10);
        t.execute();
        LocalDate payDate = LocalDate.of(2001, 11, 16); // Payday

        SalesReceiptTransaction sr = new SalesReceiptTransaction(payDate, 5000.00, empId);
        sr.execute();
        SalesReceiptTransaction sr2 = new SalesReceiptTransaction(payDate.minusDays(15), 3500.00, empId);
        sr2.execute();
        PaydayTransaction pt = new PaydayTransaction(payDate);
        pt.execute();
        validatePaycheck(pt, empId, payDate, 2000.00);
    }

    @Test
    public void salariedUnionMemberDues() {
        int empId = 1;
        AddSalariedEmployee t = new AddSalariedEmployee(empId, "Bob", "Home", 1000.00);
        t.execute();
        int memberId = 7734;
        ChangeMemberTransaction cmt = new ChangeMemberTransaction(empId, memberId, 9.42);
        cmt.execute();
        LocalDate payDate = LocalDate.of(2001, 11, 30);
        PaydayTransaction pt = new PaydayTransaction(payDate);
        pt.execute();
        Paycheck pc = pt.getPaycheck(empId);
        Assert.assertNotNull(pc);
        Assert.assertEquals(payDate, pc.payDate());
        Assert.assertEquals(1000.0, pc.grossPay(), .001);
        Assert.assertEquals("Hold", pc.getField("Disposition"));
        Assert.assertEquals(47.1, pc.deductions(), .001);
        Assert.assertEquals(1000.0 - 47.1, pc.netPay(), .001);
    }

    @Test
    public void hourlyUnionMemberServiceCharge() {
        int empId = 1;
        AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bill", "Home", 15.24);
        t.execute();
        int memberId = 7734;
        ChangeMemberTransaction cmt = new ChangeMemberTransaction(empId, memberId, 9.42);
        cmt.execute();
        LocalDate payDate = LocalDate.of(2001, 11, 9);
        ServiceChargeTransaction sct = new ServiceChargeTransaction(memberId, payDate, 19.42);
        sct.execute();
        TimeCardTransaction tct = new TimeCardTransaction(payDate, 8.0, empId);
        tct.execute();
        PaydayTransaction pt = new PaydayTransaction(payDate);
        pt.execute();
        Paycheck pc = pt.getPaycheck(empId);
        Assert.assertNotNull(pc);
        Assert.assertEquals(payDate, pc.payDate());
        Assert.assertEquals(8*15.24, pc.grossPay(), .001);
        Assert.assertEquals("Hold", pc.getField("Disposition"));
        Assert.assertEquals(9.42 + 19.42, pc.deductions(), .001);
        Assert.assertEquals((8*15.24)-(9.42 + 19.42),pc.netPay(), .001);
    }

    @Test
    public void ServiceChargesSpanningMultiplePayPeriods() {
        int empId = 1;
        AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bill", "Home", 15.24);
        t.execute();
        int memberId = 7734;
        ChangeMemberTransaction cmt = new ChangeMemberTransaction(empId, memberId, 9.42);
        cmt.execute();
        LocalDate payDate = LocalDate.of(2001, 11, 9);
        LocalDate earlyDate = LocalDate.of(2001, 11, 2); // previous Friday
        LocalDate lateDate = LocalDate.of(2001, 11, 16); // next Friday
        ServiceChargeTransaction sct = new ServiceChargeTransaction(memberId, payDate, 19.42);
        sct.execute();
        ServiceChargeTransaction sctEarly = new ServiceChargeTransaction(memberId,earlyDate,100.00);
        sctEarly.execute();
        ServiceChargeTransaction sctLate = new ServiceChargeTransaction(memberId,lateDate,200.00);
        sctLate.execute();
        TimeCardTransaction tct = new TimeCardTransaction(payDate, 8.0, empId);
        tct.execute();
        PaydayTransaction pt = new PaydayTransaction(payDate);
        pt.execute();
        Paycheck pc = pt.getPaycheck(empId);
        Assert.assertNotNull(pc);
        Assert.assertEquals(payDate, pc.payDate());
        Assert.assertEquals(8*15.24, pc.grossPay(), .001);
        Assert.assertEquals("Hold", pc.getField("Disposition"));
        Assert.assertEquals(9.42 + 19.42, pc.deductions(), .001);
        Assert.assertEquals((8*15.24) - (9.42 + 19.42), pc.netPay(), .001);
    }
}
