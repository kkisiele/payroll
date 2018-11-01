package com.kkisiele.domain;

import java.util.Set;

public interface PayrollDatabase {
    void addEmployee(int id, Employee employee);
    Employee getEmployee(int id);
    Set<Integer> getAllEmployeeIds();
    void deleteEmployee(int id);
    void addUnionMember(int memberId, Employee employee);
    Employee getUnionMember(int memberId);
    void removeUnionMember(int memberId);
}
