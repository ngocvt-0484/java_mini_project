package com.example.employeemanagement.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchEmployeeRequest {
    private String name;
    private String department;
}
