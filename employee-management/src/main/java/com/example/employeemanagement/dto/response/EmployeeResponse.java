package com.example.employeemanagement.dto.response;

import com.example.employeemanagement.enums.UserRole;
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
    private UserRole role;
    private DepartmentResponse department;
}
