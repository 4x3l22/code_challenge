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
    @NotBlank
    private String name;
    @Schema(description = "Apellidos del cliente", example = "Ramírez")
    @NotBlank
    private String lastName;
    @Schema(description = "Edad del cliente", example = "21")
    @NotBlank
    private int age;
    @Schema(description = "Fecha de nacimiento del cliente", example = "2004-06-14")
    @NotBlank
    private Date birthDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
