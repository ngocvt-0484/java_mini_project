package com.example.employeemanagement.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponse {
    private Long id;
    private String name;
    private String email;
    private DepartmentResponse department;
}
