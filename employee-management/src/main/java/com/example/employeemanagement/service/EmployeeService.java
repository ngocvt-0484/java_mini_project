package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.request.CreateEmployeeRequest;
import com.example.employeemanagement.dto.request.SearchEmployeeRequest;
import com.example.employeemanagement.dto.request.UpdateEmployeeRequest;
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

    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return mapToEmployeeResponse(employee);
    }

    public EmployeeResponse updateEmployee(Long id, UpdateEmployeeRequest employeeRequest) {
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
                throw new DuplicateResourceException("Email already exists: " + employeeRequest.getEmail());
            }
            employee.setEmail(employeeRequest.getEmail());
        }

        if (employeeRequest.getDepartmentId() != null) {
            Department department = departmentRepository.findById(employeeRequest.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + employeeRequest.getDepartmentId()));
            employee.setDepartment(department);
        }
        Employee updatedEmployee = employeeRepository.save(employee);
        return mapToEmployeeResponse(updatedEmployee);
    }

    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        employeeRepository.delete(employee);
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
