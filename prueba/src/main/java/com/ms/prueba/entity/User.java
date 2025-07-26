package com.ms.prueba.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User extends BaseEntity{

    @Column(
            name = "username",
            nullable = false,
            unique = true,
            length = 255
    )
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "rol", nullable = false, length = 100)
    private String rol;
}
