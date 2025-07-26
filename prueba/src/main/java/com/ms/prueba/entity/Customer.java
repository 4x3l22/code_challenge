package com.ms.prueba.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "clientes")
public class Customer extends BaseEntity {

    @NotBlank(message = "El nombre no puede estar vacio")
    @Size(min = 3, message = "El nombre debe tener por lo menos tres caracteres")
    @Column(name = "name", length = 500, nullable = false)
    private String name;

    @NotBlank(message = "El apellido no puede estar vacio")
    @Size(min = 3, message = "El apellido debe tener por lo menos tres caracteres")
    @Column(name = "lastName", length = 500, nullable = false)
    private String LastName;

    @Column(name = "age", length = 100, nullable = false)
    private int age;

    @Column(name = "birtDate", nullable = false)
    private Date birthDate;

}
