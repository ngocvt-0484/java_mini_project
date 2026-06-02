package com.example.employeemanagement.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentStatisticsReponse {
    private String departmentName;
    private Long employeeCount;
}
