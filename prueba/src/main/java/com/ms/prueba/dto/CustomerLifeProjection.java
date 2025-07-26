package com.ms.prueba.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class CustomerLifeProjection {
    private String name;
    private String lastName;
    private Integer age;
    private LocalDate birthDate;
    private LocalDate expectedDeathDate;

    public CustomerLifeProjection(String name, String lastName, Integer age, LocalDate birthDate, LocalDate expectedDeathDate) {

        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.birthDate = birthDate;
        this.expectedDeathDate = expectedDeathDate;
    }
}
