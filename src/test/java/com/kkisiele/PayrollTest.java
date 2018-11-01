package com.kkisiele;

import com.kkisiele.application.*;
import com.kkisiele.domain.*;
import com.kkisiele.infrastructure.InMemoryDatabase;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Map;

public class PayrollTest {
    private final PayrollDatabase database = new InMemoryDatabase();
    private final PayrollFacade facade = new PayrollFacade(database);

    @Test
    public void addSalariedEmployee() {
        int empId = 1;
        facade.addSalariedEmployee(empId, "Bob", "Home", 1000.00);

        Employee e = database.getEmployee(empId);
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
        facade.addHourlyEmployee(empId, "Bob", "Home", 10.00);

        Employee e = database.getEmployee(empId);
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
        facade.addCommissionedEmployee(empId, "Bob", "Home", 1200, 20);

        Employee e = database.getEmployee(empId);
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
        facade.addCommissionedEmployee(empId, "Bill", "Home", 2500, 3);

        Employee e = database.getEmployee(empId);
        Assert.assertNotNull(e);
        facade.deleteEmployee(empId);
        e = database.getEmployee(empId);
        Assert.assertNull(e);
    }

    @Test
    public void timeCardTransaction() {
        int empId = 5;
        facade.addHourlyEmployee(empId, "Bill", "Home", 15.25);
        facade.registerTime(LocalDate.of(2005, 7, 31), 8.0, empId);
        Employee e = database.getEmployee(empId);
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
        facade.addHourlyEmployee(empId, "Bill", "Home", 15.25);
        Employee e = database.getEmployee(empId);
        Assert.assertNotNull(e);

        UnionAffiliation af = new UnionAffiliation();
        e.setAffiliation(af);
        int memberId = 86; // Maxwell Smart
        database.addUnionMember(memberId, e);
        facade.registerCharge(memberId, LocalDate.of(2005, 8, 8), 12.95);
        ServiceCharge sc = af.getServiceCharge(LocalDate.of(2005, 8, 8));
        Assert.assertNotNull(sc);
        Assert.assertEquals(12.95, sc.amount(), 0.001);
    }

    @Test
    public void changeNameTransaction() {
        int empId = 2;
        facade.addHourlyEmployee(empId, "Bill", "Home", 15.25);
        facade.changeName(empId, "Bob");
        Employee e = database.getEmployee(empId);
        Assert.assertNotNull(e);
        Assert.assertEquals("Bob", e.name());
    }

    @Test
    public void changeHourlyTransaction() {
        int empId = 3;
        facade.addCommissionedEmployee(empId, "Lance", "Home", 2500, 3.2);

        facade.changeToHourlyEmployee(empId, 27.52);
        Employee e = database.getEmployee(empId);
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
        facade.addCommissionedEmployee(empId, "Lance", "Home", 2500, 3.2);
        facade.changeToSalariedEmployee(empId, 3000.00);
        Employee e = database.getEmployee(empId);
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
        facade.addSalariedEmployee(empId, "Bob", "Home", 2500.00);
        facade.changeToCommissionedEmployee(empId, 1250.00, 5.6);
        Employee e = database.getEmployee(empId);
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
        facade.addSalariedEmployee(empId, "Mike", "Home", 3500.00);
        facade.changeToDepositMethod(empId);
        Employee e = database.getEmployee(empId);
        Assert.assertNotNull(e);
        PaymentMethod method = e.method();
        Assert.assertNotNull(method);
        Assert.assertTrue(method instanceof DirectDepositMethod);
    }

    @Test
    public void changeHoldMethod() {
        int empId = 7;
        facade.addSalariedEmployee(empId, "Mike", "Home", 3500.00);
        facade.changeToDepositMethod(empId);
        facade.changeToHoldMethod(empId);
        Employee e = database.getEmployee(empId);
        Assert.assertNotNull(e);
        PaymentMethod method = e.method();
        Assert.assertNotNull(method);
        Assert.assertTrue(method instanceof HoldMethod);
    }

    @Test
    public void changeMailMethod() {
        int empId = 8;
        facade.addSalariedEmployee(empId, "Mike", "Home", 3500.00);
        facade.changeToMailMethod(empId);
        Employee e = database.getEmployee(empId);
        Assert.assertNotNull(e);
        PaymentMethod method = e.method();
        Assert.assertNotNull(method);
        Assert.assertTrue(method instanceof MailMethod);
    }

    @Test
    public void ChangeUnionMember() {
        int empId = 8;
        facade.addHourlyEmployee(empId, "Bill", "Home", 15.25);
        int memberId = 7743;
        facade.changeToUnionAffiliation(empId, memberId, 99.42);
        Employee e = database.getEmployee(empId);
        Assert.assertNotNull(e);
        Affiliation affiliation = e.affiliation();
        Assert.assertNotNull(affiliation);
        Assert.assertTrue(affiliation instanceof UnionAffiliation);
        UnionAffiliation uf = (UnionAffiliation)affiliation;
        Assert.assertEquals(99.42, uf.dues(), .001);
        Employee member = database.getUnionMember(memberId);
        Assert.assertNotNull(member);
        Assert.assertEquals(e, member);
    }

    @Test
    public void ChangeUnaffiliatedMember() {
        int empId = 10;
        facade.addHourlyEmployee(empId, "Bill", "Home", 15.25);

        int memberId = 7743;
        facade.changeToUnionAffiliation(empId, memberId, 99.42);
        facade.changeToUnaffiliated(empId);
        Employee e = database.getEmployee(empId);
        Assert.assertNotNull(e);
        Affiliation affiliation = e.affiliation();
        Assert.assertNotNull(affiliation);
        Assert.assertTrue(affiliation instanceof NoAffiliation);
        Employee member = database.getUnionMember(memberId);
        Assert.assertNull(member);
    }

    @Test
    public void paySingleSalariedEmployee() {
        int empId = 1;
        facade.addSalariedEmployee(empId, "Bob", "Home", 1000.00);

        LocalDate payDate = LocalDate.of(2001, 11, 30);
        Map<Integer, Paycheck> paychecks = facade.payday(payDate);
        Paycheck pc = paychecks.get(empId);
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
        facade.addHourlyEmployee(empId, "Bill", "Home", 15.25);

        LocalDate payDate = LocalDate.of(2001, 11, 9);
        Map<Integer, Paycheck> paychecks = facade.payday(payDate);
        validatePaycheck(paychecks.get(empId), payDate, 0.0);
    }

    private void validatePaycheck(Paycheck pc, LocalDate payDate, double pay) {
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
        facade.addHourlyEmployee(empId, "Bill", "Home", 15.25);

        LocalDate payDate = LocalDate.of(2001, 11, 9); // Friday
        facade.registerTime(payDate, 2.0, empId);
        Map<Integer, Paycheck> paychecks = facade.payday(payDate);
        validatePaycheck(paychecks.get(empId), payDate, 30.5);
    }

    @Test
    public void paySingleHourlyEmployeeOvertimeOneTimeCard() {
        int empId = 2;
        facade.addHourlyEmployee(empId, "Bill", "Home", 15.25);

        LocalDate payDate = LocalDate.of(2001, 11, 9); // Friday
        facade.registerTime(payDate, 9.0, empId);

        Map<Integer, Paycheck> paychecks = facade.payday(payDate);
        validatePaycheck(paychecks.get(empId), payDate, (8 + 1.5)*15.25);
    }

    @Test
    public void paySingleHourlyEmployeeOnWrongDate() {
        int empId = 2;
        facade.addHourlyEmployee(empId, "Bill", "Home", 15.25);

        LocalDate payDate = LocalDate.of(2001, 11, 8); // Thursday
        facade.registerTime(payDate, 9.0, empId);
        Map<Integer, Paycheck> paychecks = facade.payday(payDate);
        Paycheck pc = paychecks.get(empId);
        Assert.assertNull(pc);
    }

    @Test
    public void paySingleHourlyEmployeeTwoTimeCards() {
        int empId = 2;
        facade.addHourlyEmployee(empId, "Bill", "Home", 15.25);

        LocalDate payDate = LocalDate.of(2001, 11, 9); // Friday
        facade.registerTime(payDate, 2.0, empId);
        facade.registerTime(payDate.minusDays(1), 5.0,empId);
        Map<Integer, Paycheck> paychecks = facade.payday(payDate);
        validatePaycheck(paychecks.get(empId), payDate, 7*15.25);
    }

    @Test
    public void paySingleHourlyEmployeeWithTimeCardsSpanningTwoPayPeriods() {
        int empId = 2;
        facade.addHourlyEmployee(empId, "Bill", "Home", 15.25);

        LocalDate payDate = LocalDate.of(2001, 11, 9); // Friday
        LocalDate dateInPreviousPayPeriod = LocalDate.of(2001, 11, 2);
        facade.registerTime(payDate, 2.0, empId);
        facade.registerTime(dateInPreviousPayPeriod, 5.0, empId);
        Map<Integer, Paycheck> paychecks = facade.payday(payDate);
        validatePaycheck(paychecks.get(empId), payDate, 2*15.25);
    }

    @Test
    public void payingSingleCommissionedEmployeeNoReceipts() {
        int empId = 2;
        facade.addCommissionedEmployee(empId, "Bill", "Home", 1500, 10);

        LocalDate payDate = LocalDate.of(2001, 11, 16); // Payday
        Map<Integer, Paycheck> paychecks = facade.payday(payDate);
        validatePaycheck(paychecks.get(empId), payDate, 1500.0);
    }

    @Test
    public void paySingleCommissionedEmployeeOneReceipt() {
        int empId = 2;
        facade.addCommissionedEmployee(empId, "Bill", "Home", 1500, 10);
        LocalDate payDate = LocalDate.of(2001, 11, 16); // Payday

        facade.registerSale(payDate, 5000.00, empId);
        Map<Integer, Paycheck> paychecks = facade.payday(payDate);
        validatePaycheck(paychecks.get(empId), payDate, 2000.00);
    }

    @Test
    public void paySingleCommissionedEmployeeOnWrongDate() {
        int empId = 2;
        facade.addCommissionedEmployee(empId, "Bill", "Home", 1500, 10);
        LocalDate payDate = LocalDate.of(2001, 11, 9); // wrong friday

        facade.registerSale(payDate, 5000.00, empId);
        Map<Integer, Paycheck> paychecks = facade.payday(payDate);
        Paycheck pc = paychecks.get(empId);
        Assert.assertNotNull(pc);
    }

    @Test
    public void paySingleCommissionedEmployeeTwoReceipts() {
        int empId = 2;
        facade.addCommissionedEmployee(empId, "Bill", "Home", 1500, 10);
        LocalDate payDate = LocalDate.of(2001, 11, 16); // Payday

        facade.registerSale(payDate, 5000.00, empId);
        facade.registerSale(payDate.minusDays(1), 3500.00, empId);
        Map<Integer, Paycheck> paychecks = facade.payday(payDate);
        validatePaycheck(paychecks.get(empId), payDate, 2350.00);
    }

    @Test
    public void testPaySingleCommissionedEmployeeWithReceiptsSpanningTwoPayPeriods() {
        int empId = 2;
        facade.addCommissionedEmployee(empId, "Bill", "Home", 1500, 10);
        LocalDate payDate = LocalDate.of(2001, 11, 16); // Payday

        facade.registerSale(payDate, 5000.00, empId);
        facade.registerSale(payDate.minusDays(15), 3500.00, empId);
        Map<Integer, Paycheck> paychecks = facade.payday(payDate);
        validatePaycheck(paychecks.get(empId), payDate, 2000.00);
    }

    @Test
    public void salariedUnionMemberDues() {
        int empId = 1;
        facade.addSalariedEmployee(empId, "Bob", "Home", 1000.00);
        int memberId = 7734;
        facade.changeToUnionAffiliation(empId, memberId, 9.42);
        LocalDate payDate = LocalDate.of(2001, 11, 30);
        Map<Integer, Paycheck> paychecks = facade.payday(payDate);
        Paycheck pc = paychecks.get(empId);
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
        facade.addHourlyEmployee(empId, "Bill", "Home", 15.24);
        int memberId = 7734;
        facade.changeToUnionAffiliation(empId, memberId, 9.42);
        LocalDate payDate = LocalDate.of(2001, 11, 9);
        facade.registerCharge(memberId, payDate, 19.42);
        facade.registerTime(payDate, 8.0, empId);
        Map<Integer, Paycheck> paychecks = facade.payday(payDate);
        Paycheck pc = paychecks.get(empId);
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
        facade.addHourlyEmployee(empId, "Bill", "Home", 15.24);
        int memberId = 7734;
        facade.changeToUnionAffiliation(empId, memberId, 9.42);
        LocalDate payDate = LocalDate.of(2001, 11, 9);
        LocalDate earlyDate = LocalDate.of(2001, 11, 2); // previous Friday
        LocalDate lateDate = LocalDate.of(2001, 11, 16); // next Friday
        facade.registerCharge(memberId, payDate, 19.42);
        facade.registerCharge(memberId,earlyDate,100.00);
        facade.registerCharge(memberId,lateDate,200.00);
        facade.registerTime(payDate, 8.0, empId);
        Map<Integer, Paycheck> paychecks = facade.payday(payDate);
        Paycheck pc = paychecks.get(empId);
        Assert.assertNotNull(pc);
        Assert.assertEquals(payDate, pc.payDate());
        Assert.assertEquals(8*15.24, pc.grossPay(), .001);
        Assert.assertEquals("Hold", pc.getField("Disposition"));
        Assert.assertEquals(9.42 + 19.42, pc.deductions(), .001);
        Assert.assertEquals((8*15.24) - (9.42 + 19.42), pc.netPay(), .001);
    }
}
