package com.example.final20.controllers;

import com.example.final20.dao.EmployeesRepository;

import com.example.final20.dao.ProjectRepository;
import com.example.final20.entities.Employees;
import com.example.final20.entities.Project;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@AllArgsConstructor
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectRepository projectRepository;
    private final EmployeesRepository employeesRepository;

    @GetMapping
    public String showProjects(Model model) {
        List<Project> projects = projectRepository.findAll();
        model.addAttribute("projects", projects);
        return "projects";
    }

    @PostMapping("/add")
    public String addProject(@RequestParam String name) {
        Project project = new Project();
        project.setName(name);
        projectRepository.save(project);
        return "redirect:/projects";
    }

    @GetMapping("/delete")
    public String deleteProject(@RequestParam long id) {
        projectRepository.deleteById(id);
        return "redirect:/projects";
    }

    @GetMapping("/assign")
    public String assignEmployeeToProject(@RequestParam long projectId, @RequestParam long employeeId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        Optional<Employees> optionalEmployee = employeesRepository.findById((int) employeeId);

        if (optionalProject.isPresent() && optionalEmployee.isPresent()) {
            Project project = optionalProject.get();
            Employees employee = optionalEmployee.get();

            Set<Employees> employees = project.getEmployees();
            employees.add(employee);
            project.setEmployees(employees);
            projectRepository.save(project);
        }

        return "redirect:/projects";
    }

    @GetMapping("/{id}")
    public String showProjectDetails(@PathVariable long id, Model model) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isPresent()) {
            model.addAttribute("project", optionalProject.get());
            return "project_details";
        }
        return "redirect:/projects";
    }

}
