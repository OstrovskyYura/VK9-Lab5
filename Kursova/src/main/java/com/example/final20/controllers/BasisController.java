package com.example.final20.controllers;
import com.example.final20.dao.BasisRepository;
import com.example.final20.entities.Basis;
import com.example.final20.factories.EntityFactory;
import com.example.final20.strategies.InternSalaryStrategy;
import com.example.final20.strategies.SalaryStrategy;
import com.example.final20.strategies.StandardSalaryStrategy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Optional;
@Controller
@AllArgsConstructor
public class BasisController {
    private BasisRepository basisRepository;
    @GetMapping("/basis")
    public String showReception(Model model) {
        List<Basis> bases = basisRepository.findAll();
        model.addAttribute("basis", bases);
        return "basis";
    }

    @PostMapping("/add_basis")
    public String addReception(@RequestParam String time,
                               @RequestParam String name,
                               @RequestParam int total_time) {
        Basis basis = EntityFactory.createBasis(name, time, total_time);
        basisRepository.save(basis);
        return "redirect:/basis";
    }

    @GetMapping("/delete_basis")
    public String deleteReception(@RequestParam long id) {
        basisRepository.deleteById(id);
        return "redirect:/basis";
    }
    @GetMapping("/edit_basis")
    public String editReception(@RequestParam long id, Model model) {
        Optional<Basis> optionalReception = basisRepository.findById(id);
        if (optionalReception.isEmpty()) {
            return "redirect:/basis";
        }
        model.addAttribute("basis", optionalReception.get());
        return "edit_basis";
    }
    @PostMapping("/update_basis")
    public String updateReception(@RequestParam long id,
                                  @RequestParam String time,
                                  @RequestParam("name") String name,
                                  @RequestParam int total_time) {
        Optional<Basis> optionalReception = basisRepository.findById(id);
        optionalReception.ifPresent(t -> {
            t.setTotal_time(total_time);
            t.setTime(time);
            t.setName(name);
            basisRepository.save(t);
        });
        return "redirect:/basis";
    }

    @GetMapping("/basis_salary")
    public String calculateSalary(@RequestParam long id, Model model) {
        Optional<Basis> optionalReception = basisRepository.findById(id);
        if (optionalReception.isPresent()) {
            Basis basis = optionalReception.get();

            // Вибір стратегії залежно від посади
            SalaryStrategy strategy = basis.getName().equalsIgnoreCase("Intern")
                    ? new InternSalaryStrategy()
                    : new StandardSalaryStrategy();

            double salary = round(strategy.calculateSalary(basis.getTotal_time()));
            double bonus = round(salary * 0.045);
            double premium = round(salary * 0.06);
            double tax = round(salary * 0.05);
            double militaryTax = round(salary * 0.015);
            double netSalary = round(salary + bonus + premium - tax - militaryTax);


            // Створення об'єкта з відрахуваннями
            Deductions deductions = new Deductions(tax, militaryTax, netSalary);

            // Додавання атрибутів до моделі для Thymeleaf
            model.addAttribute("salary", salary);
            model.addAttribute("basis", basis);
            model.addAttribute("deductions", deductions);
            model.addAttribute("bonus", bonus);
            model.addAttribute("premium", premium);

            return "basis_salary";
        } else {
            return "redirect:/basis";
        }
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0; // Округлення до 2 знаків
    }



    private static class Deductions {
        private final double tax;
        private final double militaryTax;
        private final double netSalary;
        public Deductions(double tax, double militaryTax, double netSalary) {
            this.tax = tax;
            this.militaryTax = militaryTax;
            this.netSalary = netSalary;
        }
        public double getTax() {
            return tax;
        }
        public double getMilitaryTax() {
            return militaryTax;
        }
        public double getNetSalary() {
            return netSalary;
        }
    }
    private double calculateSalaryForReception(Basis basis) {
        int daysWorked = basis.getTotal_time();
        double hourlyRate = 475.0;
        double salary = daysWorked * hourlyRate;
        return salary;
    }
}