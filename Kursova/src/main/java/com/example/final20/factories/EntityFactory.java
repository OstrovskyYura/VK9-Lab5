package com.example.final20.factories;

import com.example.final20.entities.Basis;
import com.example.final20.entities.Employees;

public class EntityFactory {

    public static Basis createBasis(String name, String time, int total_time) {
        Basis basis = new Basis();
        basis.setName(name);
        basis.setTime(time);
        basis.setTotal_time(total_time);
        return basis;
    }

    public static Employees createEmployee(String name, String address, String phone, String email, int medicalCardNumber, String diagnosis) {
        Employees employees = new Employees();
        employees.setName(name);
        employees.setAddress(address);
        employees.setPhone(phone);
        employees.setEmail(email);
        employees.setMedicalCardNumber(medicalCardNumber);
        employees.setDiagnosis(diagnosis);
        return employees;
    }
}
