package com.example.final20.controllers;

import com.example.final20.dao.DepartmentRepository;
import com.example.final20.entities.Department;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/departments")
public class DepartmentController {
    private final DepartmentRepository departmentRepository;

    @GetMapping
    public String showDepartments(Model model) {
        List<Department> departments = departmentRepository.findAll();
        model.addAttribute("departments", departments);
        return "departments";
    }

    @PostMapping("/add")
    public String addDepartment(@RequestParam String name) {
        Department department = new Department();
        department.setName(name);
        departmentRepository.save(department);
        return "redirect:/departments";
    }

    @GetMapping("/delete")
    public String deleteDepartment(@RequestParam long id) {
        departmentRepository.deleteById(id);
        return "redirect:/departments";
    }
}

