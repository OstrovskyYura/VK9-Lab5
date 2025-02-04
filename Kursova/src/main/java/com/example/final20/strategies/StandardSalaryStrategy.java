package com.example.final20.strategies;

public class StandardSalaryStrategy implements SalaryStrategy {
    private static final double HOURLY_RATE = 475.0;

    @Override
    public double calculateSalary(int daysWorked) {
        double salary = daysWorked * HOURLY_RATE;
        double bonus = salary * 0.045;
        double premium = salary * 0.06;
        salary = salary + bonus + premium;
        double tax = salary * 0.05;
        double militaryTax = salary * 0.015;
        return salary - tax - militaryTax;
    }
}
