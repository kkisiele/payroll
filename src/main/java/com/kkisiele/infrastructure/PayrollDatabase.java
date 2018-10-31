package com.kkisiele.infrastructure;

import com.kkisiele.domain.Employee;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PayrollDatabase {
    private static final Map<Integer, Employee> employees = new HashMap<>();
    private static final Map<Integer, Employee> unionMembers = new HashMap<>();

    public static void addEmployee(int id, Employee employee) {
        employees.put(id, employee);
    }

    public static Employee getEmployee(int id) {
        return employees.get(id);
    }

    public static Set<Integer> getAllEmployeeIds() {
        return employees.keySet();
    }

    public static void deleteEmployee(int id) {
        employees.remove(id);
    }

    public static void addUnionMember(int memberId, Employee employee) {
        unionMembers.put(memberId, employee);
    }

    public static Employee getUnionMember(int memberId) {
        return unionMembers.get(memberId);
    }

    public static void removeUnionMember(int memberId) {
        unionMembers.remove(memberId);
    }
}
