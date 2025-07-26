package com.ms.prueba.controller;

import com.ms.prueba.entity.Customer;
import com.ms.prueba.service.implement.CustomerService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("api/v1/customer")
public class CustomerController extends BaseController<Customer>{
    protected CustomerController(CustomerService service) {
        super(service);
    }
}
