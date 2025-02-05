package com.example.final20.controllers;
import com.example.final20.dao.DepartmentRepository;
import com.example.final20.dao.EmployeesRepository;
import com.example.final20.dao.ProjectRepository;
import com.example.final20.entities.Department;
import com.example.final20.entities.Employees;
import com.example.final20.entities.Project;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
@AllArgsConstructor
public class EmployeesController {
    private final DepartmentRepository departmentRepository;
    private final ProjectRepository projectRepository;
    private EmployeesRepository employeesRepository;

    @GetMapping("/employees")
    public String showEmployees(Model model) {
        List<Employees> employees = employeesRepository.findByNameNotNullOrderByName();
        List<Department> departments = departmentRepository.findAll();
        model.addAttribute("employees", employees);
        model.addAttribute("departments", departments);
        return "employees";
    }

    @PostMapping("/add_employees")
    public String addEmployee(@RequestParam String name, @RequestParam String address,
                              @RequestParam String phone, @RequestParam String email,
                              @RequestParam int medical_card_number, @RequestParam String diagnosis,
                              @RequestParam(required = false) Long departmentId) {
        Employees employee = new Employees();
        employee.setName(name);
        employee.setAddress(address);
        employee.setPhone(phone);
        employee.setEmail(email);
        employee.setMedicalCardNumber(medical_card_number);
        employee.setDiagnosis(diagnosis);

        if (departmentId != null) {
            Department department = departmentRepository.findById(departmentId).orElse(null);
            employee.setDepartment(department);
        }

        employeesRepository.save(employee);
        return "redirect:/employees";
    }

    @GetMapping("/delete_employees")
    public String deletePatients(@RequestParam Long id) {
        employeesRepository.deleteById(id);
        return "redirect:/employees";
    }

    @GetMapping("/edit_employees")
    public String editPatients(@RequestParam Long id, Model model) {
        Optional<Employees> optionalPatients = employeesRepository.findById(id);
        if (optionalPatients.isEmpty()) {
            return "redirect:/employees";
        }
        model.addAttribute("employees", optionalPatients.get());
        return "edit_employees";
    }

    @PostMapping("/update_employees")
    public String updateReception(@RequestParam Long id, @RequestParam String name, @RequestParam String address,
                                  @RequestParam("phone") String phone, @RequestParam String email,
                                  @RequestParam("medical_card_number") int medical_card_number,
                                  @RequestParam String diagnosis) {
        Optional<Employees> optionalPatients = employeesRepository.findById(id);
        optionalPatients.ifPresent(t -> {
            t.setName(name);
            t.setAddress(address);
            t.setPhone(phone);
            t.setEmail(email);
            t.setMedicalCardNumber(medical_card_number);
            t.setDiagnosis(diagnosis);
            employeesRepository.save(t);
        });
        return "redirect:/employees";
    }

    @GetMapping("/project_details")
    public String viewProjectsByEmployee(@RequestParam Long employeeId, Model model) {
        Optional<Employees> optionalEmployee = employeesRepository.findById(employeeId);

        if (optionalEmployee.isEmpty()) {
            return "redirect:/employees";
        }

        Employees employee = optionalEmployee.get();
        Set<Project> projects = employee.getProjects();

        if (projects.isEmpty()) {
            return "redirect:/employees";
        }

        model.addAttribute("projects", projects);
        model.addAttribute("employee", employee);
        return "project_details";
    }


    @GetMapping("/projects/{id}/details")
    public String viewProjectDetails(@PathVariable("id") Long id, Model model) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isEmpty()) {
            return "redirect:/projects";
        }
        Project project = optionalProject.get();
        List<Employees> employees = employeesRepository.findAll();

        model.addAttribute("project", project);
        model.addAttribute("employees", employees);

        return "project_details"; // ✅ Повертаємо саме project_details.html
    }

    @PostMapping("/projects/{id}/assign_employee")
    public String assignEmployeeToProject(@PathVariable Long id, @RequestParam Long employeeId) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        Optional<Employees> optionalEmployee = employeesRepository.findById(employeeId);

        if (optionalProject.isPresent() && optionalEmployee.isPresent()) {
            Project project = optionalProject.get();
            Employees employee = optionalEmployee.get();

            project.getEmployees().add(employee);
            projectRepository.save(project);

            return "redirect:/projects/" + id + "/details";
        }
        return "redirect:/projects";
    }
}