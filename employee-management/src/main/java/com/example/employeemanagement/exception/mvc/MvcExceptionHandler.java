package com.example.employeemanagement.exception.mvc;

import com.example.employeemanagement.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(
        basePackages =
                "com.example.employeemanagement.controller.view"
)
public class MvcExceptionHandler {
    // 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(
            ResourceNotFoundException ex,
            Model model
    ) {

        model.addAttribute(
                "message",
                ex.getMessage()
        );

        return "error/404";
    }

    // 500
    @ExceptionHandler(Exception.class)
    public String handleException(
            Exception ex,
            Model model,
            HttpServletRequest request
    ) {

        model.addAttribute(
                "message",
                ex.getMessage()
        );

        model.addAttribute(
                "path",
                request.getRequestURI()
        );

        return "error/500";
    }
}
