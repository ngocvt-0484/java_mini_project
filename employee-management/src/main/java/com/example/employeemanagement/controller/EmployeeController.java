package com.example.employeemanagement.controller;

import com.example.employeemanagement.service.UtilityService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {
    private final UtilityService utilityService;
    private final PasswordEncoder passwordEncoder;

    public EmployeeController(UtilityService utilityService, PasswordEncoder passwordEncoder) {
        this.utilityService = utilityService;
        this.passwordEncoder = passwordEncoder;
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
}
