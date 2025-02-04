package com.example.final20.services;

public class SalaryCalculationService {
    private static SalaryCalculationService instance;

    private SalaryCalculationService() {}

    public static synchronized SalaryCalculationService getInstance() {
        if (instance == null) {
            instance = new SalaryCalculationService();
        }
        return instance;
    }

    public double calculateSalary(int daysWorked, double hourlyRate) {
        double salary = daysWorked * hourlyRate;
        double bonus = salary * 0.045;
        double premium = salary * 0.06;
        salary = salary + bonus + premium;
        double tax = salary * 0.05;
        double militaryTax = salary * 0.015;
        return salary - tax - militaryTax;
    }
}
