package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.request.CreateEmployeeRequest;
import com.example.employeemanagement.dto.response.EmployeeResponse;
import com.example.employeemanagement.dto.response.SuccessResponse;
import com.example.employeemanagement.service.EmployeeService;
import com.example.employeemanagement.service.UtilityService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {
    private final UtilityService utilityService;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeService employeeService;

    public EmployeeController(UtilityService utilityService, PasswordEncoder passwordEncoder, EmployeeService employeeService) {
        this.utilityService = utilityService;
        this.passwordEncoder = passwordEncoder;
        this.employeeService = employeeService;
    }

    @GetMapping("/employee/code")
    public String employeeCode() {
        return utilityService.generateEmployeeCode();
    }

    @GetMapping("/employee/name")
    public String employeeName(@RequestParam String name) {
        return utilityService.formatEmployeeName(name);
    }

    @GetMapping("/employee/password")
    public String employeePassword(@RequestParam String password) {
        return passwordEncoder.encode(password);
    }

    // GET employees
    @GetMapping("/employees")
    public ResponseEntity<SuccessResponse<List<EmployeeResponse>>> getEmployees() {
        List<EmployeeResponse> employees = employeeService.getAllEmployees();
        SuccessResponse<List<EmployeeResponse>> successResource = new SuccessResponse<>("Employees found successfully", employees);
        return ResponseEntity.ok(successResource);
    }

    // POST create employee
    @PostMapping("/employees")
    public ResponseEntity<?> createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {
        EmployeeResponse e = employeeService.createEmployee(request);
        SuccessResponse<EmployeeResponse> response = new SuccessResponse<>("Employee created successfully", e);
        return ResponseEntity.ok(response);
    }

}
