package com.example.employeemanagement.db;

import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.enums.UserRole;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import lombok.*;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class Seed implements CommandLineRunner {
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (departmentRepository.count() == 0) {
            log.info("Seeding departments table .....");
            seedDepartments();
        }

        if (employeeRepository.count() == 0) {
            log.info("Seeding employees table...");
            seedEmployees();
        }
    }

    private void seedDepartments() {

        List<Department> departments = List.of(

                Department.builder()
                        .name("IT")
                        .build(),

                Department.builder()
                        .name("HR")
                        .build(),

                Department.builder()
                        .name("Finance")
                        .build(),

                Department.builder()
                        .name("Sales")
                        .build()
        );

        departmentRepository.saveAll(departments);

        System.out.println("✓ Seeded 4 departments");
    }

    private void seedEmployees() {

        Department itDept = departmentRepository
                .findByName("IT")
                .orElseThrow();

        Department hrDept = departmentRepository
                .findByName("HR")
                .orElseThrow();

        Department financeDept = departmentRepository
                .findByName("Finance")
                .orElseThrow();

        Department salesDept = departmentRepository
                .findByName("Sales")
                .orElseThrow();

        String password = passwordEncoder.encode("123456");

        List<Employee> employees = List.of(

                Employee.builder()
                        .name("John Doe")
                        .email("john.doe@example.com")
                        .department(itDept)
                        .password(password)
                        .role(UserRole.ADMIN)
                        .build(),

                Employee.builder()
                        .name("Jane Smith")
                        .email("jane.smith@example.com")
                        .department(hrDept)
                        .password(password)
                        .role(UserRole.ADMIN)
                        .build(),

                Employee.builder()
                        .name("Bob Johnson")
                        .email("bob.johnson@example.com")
                        .password(password)
                        .role(UserRole.ADMIN)
                        .department(financeDept)
                        .build(),

                Employee.builder()
                        .name("Alice Williams")
                        .email("alice.williams@example.com")
                        .password(password)
                        .role(UserRole.ADMIN)
                        .department(salesDept)
                        .build(),

                Employee.builder()
                        .name("Charlie Brown")
                        .email("charlie.brown@example.com")
                        .password(password)
                        .role(UserRole.ADMIN)
                        .department(itDept)
                        .build(),

                Employee.builder()
                        .name("Diana Prince")
                        .email("diana.prince@example.com")
                        .password(password)
                        .role(UserRole.USER)
                        .department(salesDept)
                        .build(),

                Employee.builder()
                        .name("Eve Davis")
                        .email("eve.davis@example.com")
                        .password(password)
                        .role(UserRole.USER)
                        .department(hrDept)
                        .build()
        );

        employeeRepository.saveAll(employees);

        System.out.println("✓ Seeded 7 employees");
    }
}
