package com.example.employeemanagement.repository;

import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.projection.DepartmentStatisticsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    @Query("""
       SELECT d.name as departmentName,
              COUNT(e.id) as employeeCount
       FROM Employee e
       JOIN e.department d
       GROUP BY d.name
       """)
    List<DepartmentStatisticsProjection>
    getEmployeeStatisticsByDepartment();
}
