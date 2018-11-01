package com.kkisiele.domain;

public class EmployeeFactory {
    public Employee createSalariedEmployee(int empId, String name, String address, double salary) {
        Employee employee = new Employee(empId, name, address);
        employee.setClassification(new SalariedClassification(salary));
        employee.setSchedule(new MonthlySchedule());
        employee.setMethod(new HoldMethod());
        return employee;
    }

    public Employee createHourlyEmployee(int empId, String name, String address, double hourRate) {
        Employee employee = new Employee(empId, name, address);
        employee.setClassification(new HourlyClassification(hourRate));
        employee.setSchedule(new WeeklySchedule());
        employee.setMethod(new HoldMethod());
        return employee;
    }

    public Employee createCommissionedEmployee(int empId, String name, String address, double baseSalary, double commissionRate) {
        Employee employee = new Employee(empId, name, address);
        employee.setClassification(new CommissionClassification(baseSalary, commissionRate));
        employee.setSchedule(new BiWeeklySchedule());
        employee.setMethod(new HoldMethod());
        return employee;
    }
}
