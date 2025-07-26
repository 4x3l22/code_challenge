package com.ms.prueba.service.implement;

import com.ms.prueba.dto.UserDto;
import com.ms.prueba.entity.User;
import com.ms.prueba.repository.interfaces.BaseRepository;
import com.ms.prueba.repository.interfaces.UserRepository;
import com.ms.prueba.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService<User> implements IUserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository, AuditoriaService auditService) {
        super(userRepository, auditService); // inyecta en la clase base
        this.userRepository = userRepository; // inyecta en esta clase
    }

    @Override
    public UserDto authenticate(String username, String rawPassword) {
        UserDto user = userRepository.getUserWithRole(username, rawPassword);

        if (user == null) {
            // Usuario o contraseña incorrectos
            return null;
        }
        System.out.println(user.getPassword());
        // Aquí podrías agregar una validación adicional si se encripta la contraseña
        return user;
    }





}
