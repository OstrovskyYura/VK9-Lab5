package com.example.final20.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "basis")
public class Basis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "time", nullable = false, length = 100)
    private String time;
    @Column(name = "total_time", nullable = false, length = 100)
    private Integer total_time;
    @Column(name = "name", nullable = false, length = 100)
    private String name;
}