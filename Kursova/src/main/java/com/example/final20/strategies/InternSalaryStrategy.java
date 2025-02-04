package com.example.final20.strategies;

public class InternSalaryStrategy implements SalaryStrategy {
    private static final double HOURLY_RATE = 250.0;

    @Override
    public double calculateSalary(int daysWorked) {
        double salary = daysWorked * HOURLY_RATE;
        double tax = salary * 0.05;
        double militaryTax = salary * 0.015;
        return salary - tax - militaryTax;
    }
}
