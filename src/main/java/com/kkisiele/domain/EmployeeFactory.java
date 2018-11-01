package com.kkisiele.domain;

public class EmployeeFactory {
    public Employee createSalariedEmployee(int empId, String name, String address, double salary) {
        return new Employee(empId, name, address, new SalariedClassification(salary), new MonthlySchedule(), new HoldMethod());
    }

    public Employee createHourlyEmployee(int empId, String name, String address, double hourRate) {
        return new Employee(empId, name, address, new HourlyClassification(hourRate), new WeeklySchedule(), new HoldMethod());
    }

    public Employee createCommissionedEmployee(int empId, String name, String address, double baseSalary, double commissionRate) {
        return new Employee(empId, name, address, new CommissionClassification(baseSalary, commissionRate), new BiWeeklySchedule(), new HoldMethod());
    }
}
