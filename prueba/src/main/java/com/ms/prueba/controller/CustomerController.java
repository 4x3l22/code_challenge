package com.ms.prueba.controller;

import com.ms.prueba.dto.CustomerDto;
import com.ms.prueba.dto.CustomerLifeProjection;
import com.ms.prueba.entity.Customer;
import com.ms.prueba.repository.interfaces.AgeStatsProjection;
import com.ms.prueba.service.implement.BaseService;
import com.ms.prueba.service.implement.CustomerService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("api/v1/customer")
public class CustomerController extends BaseController<Customer>{

    protected CustomerService customerService;

    protected CustomerController(BaseService<Customer> service, CustomerService customerService) {
        super(service);
        this.customerService = customerService;
    }

    @Override
    @Hidden
    public ResponseEntity<Customer> save(@RequestBody Customer entity) {
        throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Operación no permitida para productos");
    }


    @PostMapping("/createUser")
    public ResponseEntity<String> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        try{
            customerService.createCustomer(customerDto);
            return ResponseEntity.ok("Customer created");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/age-stats")
    public ResponseEntity<AgeStatsProjection> getAgeStats() {
        return ResponseEntity.ok(customerService.getAgeStats());
    }

    @GetMapping("/life-expectancy")
    public ResponseEntity<List<CustomerLifeProjection>> getCustomersWithExpectedDeathDate() {
        return ResponseEntity.ok(customerService.getCustomersWithLifeExpectancy());
    }
}
