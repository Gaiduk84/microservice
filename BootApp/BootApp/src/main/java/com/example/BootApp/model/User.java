package com.example.BootApp.model;


import jakarta.persistence.*;
import lombok.*;


@Data
@Entity
@Table(name = "users")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstname;

    @Column(name = "email")
    private String email;

    @Column(name = "password", nullable = false,unique = true)
    private String password;
}
