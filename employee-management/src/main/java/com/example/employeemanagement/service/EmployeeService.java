package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.request.CreateEmployeeRequest;
import com.example.employeemanagement.dto.request.RegisterRequest;
import com.example.employeemanagement.dto.request.SearchEmployeeRequest;
import com.example.employeemanagement.dto.request.UpdateEmployeeRequest;
import com.example.employeemanagement.dto.response.DepartmentResponse;
import com.example.employeemanagement.dto.response.EmployeeReportResponse;
import com.example.employeemanagement.dto.response.EmployeeResponse;
import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.enums.UserRole;
import com.example.employeemanagement.exception.DuplicateResourceException;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    public List<EmployeeResponse> getAllEmployees(SearchEmployeeRequest request) {
        String name = request.getName();
        String department = request.getDepartment();
        List<Employee> employees;
        // Implement search logic based on request parameters

        // search by name + department
        if (name != null && department != null) {

            employees =
                    employeeRepository
                            .findByNameContainingIgnoreCaseAndDepartment_NameContainingIgnoreCase(
                                    name,
                                    department
                            );
        }

        // search by name
        else if (name != null) {
            employees =
                    employeeRepository
                            .findByNameContainingIgnoreCase(
                                    name
                            );
        }

        // search by department
        else if (department != null) {
            employees = employeeRepository
                            .findByDepartment_NameContainingIgnoreCase(
                                    department
                            );
        }
        // get all
        else {
            employees = employeeRepository.findAll();
        }
        return employees.stream()
                .map(this::mapToEmployeeResponse)
                .toList();
    }

    @CacheEvict(value = "employeeReport", allEntries = true)
    public EmployeeResponse createEmployee(CreateEmployeeRequest employeeRequest) {
        log.info("Create employee with email: {}", employeeRequest.getEmail());
        Department department = null;
        if (employeeRequest.getDepartmentId() != null) {
            department = departmentRepository.findById(employeeRequest.getDepartmentId())
                    .orElseThrow(() -> {
                        log.error("Department not found with id: {}", employeeRequest.getDepartmentId());
                        return new ResourceNotFoundException("Department not found with id: " + employeeRequest.getDepartmentId());
                    });
        }
        if (employeeRepository.existsByEmail(employeeRequest.getEmail())) {
            log.error("Email already exists: {}", employeeRequest.getEmail());
            throw new DuplicateResourceException("Email already exists: " + employeeRequest.getEmail());
        }

        Employee employee = Employee.builder()
                .name(employeeRequest.getName())
                .email(employeeRequest.getEmail())
                .password(passwordEncoder.encode(employeeRequest.getPassword()))
                .role(employeeRequest.getRole() != null ? employeeRequest.getRole() : UserRole.USER)
                .department(department)
                .build();
        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Employee created successfully with id: {}", savedEmployee.getId());
        return mapToEmployeeResponse(savedEmployee);
    }

    public void register(RegisterRequest request) {
        if (employeeRepository.existsByEmail(request.getEmail())) {
            log.error("Email already exists: {}", request.getEmail());
            throw new DuplicateResourceException("Email already exists: " + request.getEmail());
        }
        Employee employee = Employee.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.USER)
                .build();
        employeeRepository.save(employee);
    }

    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Employee not found with id: {}", id);
                    return new ResourceNotFoundException("Employee not found with id: " + id);
                });
        return mapToEmployeeResponse(employee);
    }

    @CacheEvict(value = "employeeReport", allEntries = true)
    public EmployeeResponse updateEmployee(Long id, UpdateEmployeeRequest employeeRequest) {
        log.info("Update employee with email: {}", employeeRequest.getEmail());
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        if (employeeRequest.getName() != null) {
            employee.setName(employeeRequest.getName());
        }

        if (employeeRequest.getEmail() != null) {
            boolean emailExists =
                    employeeRepository.existsByEmail(
                            employeeRequest.getEmail()
                    );
            if (emailExists && !employee.getEmail().equals(employeeRequest.getEmail())) {
                log.error("Email already exists: {}", employeeRequest.getEmail());
                throw new DuplicateResourceException("Email already exists: " + employeeRequest.getEmail());
            }
            employee.setEmail(employeeRequest.getEmail());
        }

        if (employeeRequest.getDepartmentId() != null) {
            Department department = departmentRepository.findById(employeeRequest.getDepartmentId())
                    .orElseThrow(() -> {
                                log.error("Department not found with id: {}", employeeRequest.getDepartmentId());
                                return new ResourceNotFoundException("Department not found with id: " + employeeRequest.getDepartmentId());
                    });
            employee.setDepartment(department);
        }

        if (employeeRequest.getRole() != null) {
            employee.setRole(employeeRequest.getRole());
        }

        if (employeeRequest.getPassword() != null) {
            employee.setPassword(passwordEncoder.encode(employeeRequest.getPassword()));
        }

        Employee updatedEmployee = employeeRepository.save(employee);
        log.info("Employee updated successfully with id: {}", updatedEmployee.getId());
        return mapToEmployeeResponse(updatedEmployee);
    }

    @CacheEvict(value = "employeeReport", allEntries = true)
    public void deleteEmployee(Long id) {
        log.info("Delete employee with id: {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        employeeRepository.delete(employee);
        log.info("Employee deleted successfully with id: {}", id);
    }

    @Cacheable(value = "employeeReport")
    public EmployeeReportResponse getEmployeeReport() {
        log.info("Generating employee report...");
        Long total = employeeRepository.count();
        return EmployeeReportResponse.builder().totalEmployees(total).build();
    }

    private EmployeeResponse mapToEmployeeResponse(
            Employee employee
    ) {
        DepartmentResponse departmentResponse = null;
        if (employee.getDepartment() != null) {
            departmentResponse =
                    DepartmentResponse.builder()
                            .id(employee.getDepartment().getId())
                            .name(employee.getDepartment().getName())
                            .build();
        }

        return EmployeeResponse.builder()
                .id(employee.getId())
                .name(employee.getName())
                .email(employee.getEmail())
                .department(departmentResponse)
                .role(employee.getRole())
                .build();
    }
}
