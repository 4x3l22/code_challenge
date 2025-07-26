package com.ms.prueba.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@Schema(description = "Datos de entrada para crear un cliente")
public class CustomerDto {

    @Schema(description = "Nombres del cliente", example = "Juan")
    private String name;
    @Schema(description = "Apellidos del cliente", example = "Ramírez")
    private String lastName;
    @Schema(description = "Edad del cliente", example = "21")
    private int age;
    @Schema(description = "Fecha de nacimiento del cliente", example = "2004-06-14T18:37:36.215Z")
    private Date birthDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
