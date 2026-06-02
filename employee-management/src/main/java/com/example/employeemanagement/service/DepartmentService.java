package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.response.DepartmentResponse;
import com.example.employeemanagement.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public List<DepartmentResponse> getAllDepartments() {
        List<DepartmentResponse> departmentResponses = departmentRepository.findAll()
                .stream()
                .map(department -> DepartmentResponse.builder()
                        .id(department.getId())
                        .name(department.getName())
                        .build())
                .toList();
        return departmentResponses;
    }
}
