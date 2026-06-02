package com.example.employeemanagement.dto.request;

import com.example.employeemanagement.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateEmployeeRequest {
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Email(message = "Email should be valid")
    private String email;

    private Long departmentId;

    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    private String password;

    private UserRole role;
}
