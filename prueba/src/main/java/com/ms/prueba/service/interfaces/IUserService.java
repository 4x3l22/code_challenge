package com.ms.prueba.service.interfaces;

import com.ms.prueba.dto.UserDto;
import com.ms.prueba.entity.Customer;
import com.ms.prueba.entity.User;

public interface IUserService extends IBaseService<User> {

    UserDto authenticate(String username, String rawPassword);

}
