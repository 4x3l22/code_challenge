package com.ms.prueba.controller;

import com.ms.prueba.dto.ApiResponseDto;
import com.ms.prueba.dto.CustomerDto;
import com.ms.prueba.dto.CustomerLifeProjection;
import com.ms.prueba.entity.Customer;
import com.ms.prueba.repository.interfaces.AgeStatsProjection;
import com.ms.prueba.service.implement.BaseService;
import com.ms.prueba.service.implement.CustomerService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@Tag(name = "Clientes", description = "Operaciones relacionadas con clientes")
@RequestMapping("api/v1/customer")
public class CustomerController extends BaseController<Customer>{

    protected CustomerService customerService;

    protected CustomerController(BaseService<Customer> service, CustomerService customerService) {
        super(service);
        this.customerService = customerService;
    }
//    Apis que son ignoradas
    @Override
    @Hidden
    public ResponseEntity<ApiResponseDto<Customer>> save(@RequestBody Customer entity) {
        throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Operación no permitida para productos");
    }
    @Override
    @Hidden
    public ResponseEntity<ApiResponseDto<List<Customer>>> all() {
        throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Operacion no permitida para productos");
    }

//    Apis funcionales
    @Operation(
            summary = "Crear un nuevo cliente",
            description = "Registra un cliente en la base de datos con nombre, edad, fecha de nacimiento, etc."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente creado exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/createCustomer")
    public ResponseEntity<ApiResponseDto<Optional<CustomerDto>>> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        try {
            Optional<CustomerDto> createdCustomer = customerService.createCustomer(customerDto);

            ApiResponseDto<Optional<CustomerDto>> response = new ApiResponseDto<>(
                    "Customer created",
                    createdCustomer,
                    true
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ApiResponseDto<Optional<CustomerDto>> errorResponse = new ApiResponseDto<>(
                    "Error: " + e.getMessage(),
                    Optional.empty(),
                    false
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @Operation(
            summary = "Obtener estadísticas de edad de los clientes",
            description = "Devuelve el promedio de edad y la desviación estándar de todos los clientes registrados en la base de datos."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno al obtener las estadísticas")
    })
    @GetMapping("/age-stats")
    public ResponseEntity<ApiResponseDto<AgeStatsProjection>> getAgeStats() {
        try {
            AgeStatsProjection stats = customerService.getAgeStats();

            ApiResponseDto<AgeStatsProjection> response = new ApiResponseDto<>(
                    "Age statistics retrieved successfully",
                    stats,
                    true
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponseDto<AgeStatsProjection> errorResponse = new ApiResponseDto<>(
                    "Error: " + e.getMessage(),
                    null,
                    false
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @Operation(
            summary = "Listar clientes con fecha estimada de muerte",
            description = "Devuelve una lista de todos los clientes registrados con sus datos completos, incluyendo una fecha estimada de fallecimiento basada en la esperanza de vida calculada."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno al procesar los datos")
    })
    @GetMapping("/life-expectancy")
    public ResponseEntity<ApiResponseDto<List<CustomerLifeProjection>>> getCustomersWithExpectedDeathDate() {
        try {
            List<CustomerLifeProjection> data = customerService.getCustomersWithLifeExpectancy();

            ApiResponseDto<List<CustomerLifeProjection>> response = new ApiResponseDto<>(
                    "Customers with expected death date retrieved successfully",
                    data,
                    true
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponseDto<List<CustomerLifeProjection>> errorResponse = new ApiResponseDto<>(
                    "Error: " + e.getMessage(),
                    null,
                    false
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

}
