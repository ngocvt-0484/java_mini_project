package com.example.employeemanagement.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UtilityService {
    // format ten nhan vien
    public String formatEmployeeName(String name) {
        return name.trim().toUpperCase();
    }

    // tao ma nhan vien tu dong
    public String generateEmployeeCode() {
        String time = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "E" + time + random;
    }
}
