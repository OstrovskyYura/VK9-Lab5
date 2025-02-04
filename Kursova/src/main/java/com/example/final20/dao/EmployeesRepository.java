package com.example.final20.dao;
import com.example.final20.entities.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface EmployeesRepository extends JpaRepository<Employees, Integer> {
    List<Employees> findByNameNotNullOrderByName();
}
