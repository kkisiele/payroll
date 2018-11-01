package com.kkisiele.application;

import com.kkisiele.domain.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class PayrollFacade {
    private final PayrollDatabase database;

    public PayrollFacade(PayrollDatabase database) {
        this.database = database;
    }

    public void addCommissionedEmployee(int empId, String name, String address, double baseSalary, double commissionRate) {
        EmployeeFactory factory = new EmployeeFactory();
        Employee employee = factory.createCommissionedEmployee(empId, name, address, baseSalary, commissionRate);
        database.addEmployee(empId, employee);
    }

    public void addHourlyEmployee(int empId, String name, String address, double hourRate) {
        EmployeeFactory factory = new EmployeeFactory();
        Employee employee = factory.createHourlyEmployee(empId, name, address, hourRate);
        database.addEmployee(empId, employee);
    }

    public void addSalariedEmployee(int empId, String name, String address, double salary) {
        EmployeeFactory factory = new EmployeeFactory();
        Employee employee = factory.createSalariedEmployee(empId, name, address, salary);
        database.addEmployee(empId, employee);
    }

    public void changeToCommissionedEmployee(int empId, double baseSalary, double commissionRate) {
        Employee employee = database.getEmployee(empId);
        if(employee != null) {
            employee.changeClassification(new CommissionClassification(baseSalary, commissionRate));
            employee.changeSchedule(new BiWeeklySchedule());
        }
    }

    public void changeToDepositMethod(int empId) {
        Employee e = database.getEmployee(empId);
        e.changeMethod(new DirectDepositMethod("mBank", "123"));
    }

    public void changeToHoldMethod(int empId) {
        Employee employee = database.getEmployee(empId);
        employee.changeMethod(new HoldMethod());
    }

    public void changeToHourlyEmployee(int empId, double hourRate) {
        Employee employee = database.getEmployee(empId);
        if(employee != null) {
            employee.changeClassification(new HourlyClassification(hourRate));
            employee.changeSchedule(new WeeklySchedule());
        }
    }

    public void changeToMailMethod(int empId) {
        Employee employee = database.getEmployee(empId);
        employee.changeMethod(new MailMethod("1 Infinite Loop"));
    }

    public void changeToUnionAffiliation(int empId, int memberId, double dues) {
        Employee employee = database.getEmployee(empId);
        database.addUnionMember(memberId, employee);
        employee.setAffiliation(new UnionAffiliation(memberId, dues));
    }

    public void changeName(int empId, String newName) {
        Employee e = database.getEmployee(empId);
        if(e != null) {
            e.setName(newName);
        }
    }

    public void changeToSalariedEmployee(int empId, double salary) {
        Employee employee = database.getEmployee(empId);
        if(employee != null) {
            employee.changeClassification(new SalariedClassification(salary));
            employee.changeSchedule(new MonthlySchedule());
        }
    }

    public void changeToUnaffiliated(int empId) {
        Employee employee = database.getEmployee(empId);
        if(employee.affiliation() instanceof UnionAffiliation) {
            UnionAffiliation ua = (UnionAffiliation) employee.affiliation();
            database.removeUnionMember(ua.memberId());
        }
        employee.setAffiliation(new NoAffiliation());
    }

    public void deleteEmployee(int empId) {
        database.deleteEmployee(empId);
    }

    public Map<Integer, Paycheck> payday(LocalDate payDate) {
        Map<Integer, Paycheck> paychecks = new HashMap<>();

        for(Integer empId : database.getAllEmployeeIds()) {
            Employee employee = database.getEmployee(empId);
            if(employee.isPayDate(payDate)) {
                LocalDate startDate = employee.getPayPeriodStartDate(payDate);
                Paycheck pc = new Paycheck(startDate, payDate);
                paychecks.put(empId, pc);
                employee.payday(pc);
            }
        }
        return paychecks;
    }

    public void registerSale(LocalDate date, double saleAmount, int empId) {
        Employee employee = database.getEmployee(empId);
        employee.registerSale(date, saleAmount);
    }

    public void registerCharge(int memberId, LocalDate date, double charge) {
        Employee employee = database.getUnionMember(memberId);
        employee.registerCharge(date, charge);
    }

    public void registerTime(LocalDate date, double hours, int empId) {
        Employee employee = database.getEmployee(empId);
        employee.registerTime(date, hours);
    }
}
