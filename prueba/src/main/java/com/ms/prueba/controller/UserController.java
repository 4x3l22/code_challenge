package com.ms.prueba.controller;

import com.ms.prueba.entity.User;
import com.ms.prueba.service.implement.BaseService;
import com.ms.prueba.service.implement.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("api/v1/user")
public class UserController extends BaseController<User>{

    protected UserController(BaseService<User> service) {
        super(service);
    }



}
