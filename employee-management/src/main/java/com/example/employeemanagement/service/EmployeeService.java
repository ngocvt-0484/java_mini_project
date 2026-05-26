package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.request.CreateEmployeeRequest;
import com.example.employeemanagement.dto.response.DepartmentResponse;
import com.example.employeemanagement.dto.response.EmployeeResponse;
import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.exception.DuplicateResourceException;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public List<EmployeeResponse> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(this::mapToEmployeeResponse)
                .toList();
    }

    public EmployeeResponse createEmployee(CreateEmployeeRequest employeeRequest) {
        Department department = departmentRepository.findById(employeeRequest.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + employeeRequest.getDepartmentId()));
        if (employeeRepository.existsByEmail(employeeRequest.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + employeeRequest.getEmail());
        }

        Employee employee = Employee.builder()
                .name(employeeRequest.getName())
                .email(employeeRequest.getEmail())
                .department(department)
                .build();
        Employee savedEmployee = employeeRepository.save(employee);
        return mapToEmployeeResponse(savedEmployee);
    }

    private EmployeeResponse mapToEmployeeResponse(
            Employee employee
    ) {

        return EmployeeResponse.builder()
                .id(employee.getId())
                .name(employee.getName())
                .email(employee.getEmail())
                .department(
                        DepartmentResponse.builder()
                                .id(employee.getDepartment().getId())
                                .name(employee.getDepartment().getName())
                                .build()
                )
                .build();
    }
}
