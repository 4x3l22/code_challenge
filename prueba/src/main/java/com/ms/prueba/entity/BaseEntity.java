package com.ms.prueba.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "createAdd", nullable = false)
    private LocalDateTime createAdd;

    @Column(name = "updateAdd", nullable = false)
    private LocalDateTime updateAdd;

    @Column(name = "deleteAdd", nullable = true)
    private LocalDateTime deleteAdd;

}
