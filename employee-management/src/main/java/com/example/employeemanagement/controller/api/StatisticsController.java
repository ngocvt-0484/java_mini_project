package com.example.employeemanagement.controller.api;

import com.example.employeemanagement.dto.response.DepartmentStatisticsReponse;
import com.example.employeemanagement.dto.response.EmployeeResponse;
import com.example.employeemanagement.dto.response.SuccessResponse;
import com.example.employeemanagement.dto.response.TotalEmployeeReponse;
import com.example.employeemanagement.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private final EmployeeService employeeService;

    @GetMapping("/departments")
    public ResponseEntity<SuccessResponse<List<DepartmentStatisticsReponse>>> getDepartmentStatistics() {
        SuccessResponse<List<DepartmentStatisticsReponse>> successResource = new SuccessResponse<>("Department statistics retrieved successfully",
                employeeService.getEmployeeCountByDepartment());
        return ResponseEntity.ok(successResource);
    }

    @GetMapping("/employees/total")
    public ResponseEntity<SuccessResponse<TotalEmployeeReponse>> getTotalEmployees() {
        TotalEmployeeReponse totalEmployees = new TotalEmployeeReponse(employeeService.getTotalEmployee());
        SuccessResponse<TotalEmployeeReponse> successResource = new SuccessResponse<>("Total employees retrieved successfully", totalEmployees);
        return ResponseEntity.ok(successResource);
    }
}
