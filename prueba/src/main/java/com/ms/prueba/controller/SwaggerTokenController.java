package com.ms.prueba.controller;

import com.ms.prueba.service.implement.TokenStorage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(
        name = "SwaggerToken",
        description = "Proporciona un endpoint para obtener el token JWT almacenado, el cual puede ser utilizado como autorización Bearer en futuras peticiones a la API."
)
@RequestMapping("api/v1/swagger-token")
public class SwaggerTokenController {

    private final TokenStorage tokenStorage;

    public SwaggerTokenController(TokenStorage tokenStorage) {
        this.tokenStorage = tokenStorage;
    }

    @Operation(summary = "Obtener token almacenado", description = "Devuelve el token JWT almacenado para usar en Swagger UI")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token encontrado correctamente"),
            @ApiResponse(responseCode = "204", description = "No se encontró ningún token"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/swagger-token")
    public ResponseEntity<String> getToken() {
        String token = tokenStorage.readToken();
        return token != null ? ResponseEntity.ok(token) : ResponseEntity.noContent().build();
    }
}
