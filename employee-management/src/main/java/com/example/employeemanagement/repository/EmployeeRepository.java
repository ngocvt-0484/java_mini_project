package com.example.employeemanagement.repository;

import com.example.employeemanagement.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByEmail(String email);
    Optional<Employee> findByEmail(String email);

    List<Employee> findByNameContainingIgnoreCaseAndDepartment_NameContainingIgnoreCase(
            String name,
            String department
    );
    List<Employee> findByNameContainingIgnoreCase(String name);
    List<Employee> findByDepartment_NameContainingIgnoreCase(String department);
}
