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

    @PostMapping("/projects/{id}/assign_employee")
    public String assignEmployeeToProject(@PathVariable("id") Long id, @RequestParam Long employeeId) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        Optional<Employees> optionalEmployee = employeesRepository.findById(employeeId);

        if (optionalProject.isPresent() && optionalEmployee.isPresent()) {
            Project project = optionalProject.get();
            Employees employee = optionalEmployee.get();

            if (!project.getEmployees().contains(employee)) {
                Set<Employees> employees = project.getEmployees();
                employees.add(employee);
                project.setEmployees(employees);
                projectRepository.save(project);
            }
        }
        return "redirect:/projects/" + id;
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

    @GetMapping("/{id}/assign_employee")
    public String showAssignEmployeePage(@PathVariable long id, Model model) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isPresent()) {
            List<Employees> employees = employeesRepository.findAll();
            model.addAttribute("project", optionalProject.get());
            model.addAttribute("employees", employees);
            return "project_details";
        }
        return "redirect:/projects";
    }

    @GetMapping("/remove_employee")
    public String removeEmployeeFromProject(@RequestParam long projectId, @RequestParam long employeeId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        Optional<Employees> optionalEmployee = employeesRepository.findById(employeeId);

        if (optionalProject.isPresent() && optionalEmployee.isPresent()) {
            Project project = optionalProject.get();
            Employees employee = optionalEmployee.get();

            Set<Employees> employees = project.getEmployees();
            employees.remove(employee);
            project.setEmployees(employees);
            projectRepository.save(project);
        }
        return "redirect:/projects/{projectId}";
    }
}
