package com.ms.prueba.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
public class CustomerDto {

    private String name;
    private String lastName;
    private int age;
    private Date birthDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
