package com.example.employeemanagement.controller.view;

import com.example.employeemanagement.dto.request.CreateEmployeeRequest;
import com.example.employeemanagement.dto.request.SearchEmployeeRequest;
import com.example.employeemanagement.dto.request.UpdateEmployeeRequest;
import com.example.employeemanagement.dto.response.DepartmentResponse;
import com.example.employeemanagement.dto.response.EmployeeResponse;
import com.example.employeemanagement.exception.DuplicateResourceException;
import com.example.employeemanagement.service.DepartmentService;
import com.example.employeemanagement.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeViewController {
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    /**
     * LIST PAGE
     * GET /employees
     */
    @GetMapping
    public String listEmployees(SearchEmployeeRequest searchRequest, Model model) {
        List<EmployeeResponse> employees =
                employeeService.getAllEmployees(searchRequest);

        model.addAttribute("employees", employees);
        model.addAttribute("searchRequest", searchRequest);
        return "employees/list";
    }

    /**
     * ADD FORM
     * GET /employees/add
     */
    @GetMapping("/add")
    public String addEmployee(Model model) {
        List<DepartmentResponse> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);
        model.addAttribute("employee", new CreateEmployeeRequest());

        return "employees/add";
    }

    /**
     * CREATE EMPLOYEE
     * POST /employees
     */
    @PostMapping
    public String addEmployee(
            @Valid
            @ModelAttribute("employee")
            CreateEmployeeRequest request,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        // validation error
        if (result.hasErrors()) {
            model.addAttribute(
                    "departments",
                    departmentService.getAllDepartments()
            );
            return "employees/add";
        }

        try {
            employeeService.createEmployee(request);
            // flash success
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Employee created successfully"
            );
            return "redirect:/employees";
        } catch (DuplicateResourceException ex) {
            result.rejectValue(
                    "email",
                    "error.employee",
                    ex.getMessage()
            );

            model.addAttribute(
                    "departments",
                    departmentService.getAllDepartments()
            );
            return "employees/add";
        }
    }

    /**
     * EDIT FORM
     * GET /employees/{id}/edit
     */
    @GetMapping("/{id}/edit")
    public String editEmployee(
            @PathVariable Long id,
            Model model
    ) {
        EmployeeResponse employee =
                employeeService.getEmployeeById(id);

        UpdateEmployeeRequest request =
                new UpdateEmployeeRequest();

        request.setName(employee.getName());
        request.setEmail(employee.getEmail());

        if (employee.getDepartment() != null) {

            request.setDepartmentId(
                    employee.getDepartment().getId()
            );
        }

        model.addAttribute(
                "employee",
                request
        );

        model.addAttribute(
                "employeeId",
                id
        );

        model.addAttribute(
                "departments",
                departmentService.getAllDepartments()
        );
        return "employees/edit";
    }

    /**
     * PUT EMPLOYEE
     * PUT /employees/{id}
     */
    @PutMapping("/{id}")
    public String editEmployee(
            @PathVariable Long id,
            @Valid
            @ModelAttribute("employee")
            UpdateEmployeeRequest request,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        if (result.hasErrors()) {
            model.addAttribute(
                    "departments",
                    departmentService.getAllDepartments()
            );
            return "employees/edit";
        }

        try {
            employeeService.updateEmployee(id, request);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Employee updated successfully"
            );
            return "redirect:/employees";

        } catch (DuplicateResourceException ex) {
            result.rejectValue(
                    "email",
                    "error.employee",
                    ex.getMessage()
            );
            model.addAttribute(
                    "departments",
                    departmentService.getAllDepartments()
            );

            return "employees/edit";
        }
    }

    /**
     * DELETE EMPLOYEE
     * DELETE /employees/{id}
     */
    @DeleteMapping("/{id}")
    public String deleteEmployee(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        employeeService.deleteEmployee(id);
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Employee deleted successfully"
        );
        return "redirect:/employees";
    }
}
