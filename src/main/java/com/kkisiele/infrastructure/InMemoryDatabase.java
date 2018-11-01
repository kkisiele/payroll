package com.kkisiele.infrastructure;

import com.kkisiele.domain.Employee;
import com.kkisiele.domain.PayrollDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InMemoryDatabase implements PayrollDatabase {
    private static final Map<Integer, Employee> employees = new HashMap<>();
    private static final Map<Integer, Employee> unionMembers = new HashMap<>();

    @Override
    public void addEmployee(int id, Employee employee) {
        employees.put(id, employee);
    }

    @Override
    public Employee getEmployee(int id) {
        return employees.get(id);
    }

    @Override
    public Set<Integer> getAllEmployeeIds() {
        return employees.keySet();
    }

    @Override
    public void deleteEmployee(int id) {
        employees.remove(id);
    }

    @Override
    public void addUnionMember(int memberId, Employee employee) {
        unionMembers.put(memberId, employee);
    }

    @Override
    public Employee getUnionMember(int memberId) {
        return  unionMembers.get(memberId);
    }

    @Override
    public void removeUnionMember(int memberId) {
        unionMembers.remove(memberId);
    }
}
