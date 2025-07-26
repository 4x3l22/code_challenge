package com.ms.prueba.service.implement;

import com.ms.prueba.dto.UserDto;
import com.ms.prueba.entity.User;
import com.ms.prueba.repository.interfaces.BaseRepository;
import com.ms.prueba.repository.interfaces.UserRepository;
import com.ms.prueba.service.interfaces.IUserService;
import org.springframework.stereotype.Service;

/**
 * Servicio encargado de la lógica de negocio relacionada con los usuarios del sistema.
 * Extiende de {@link BaseService} para heredar operaciones CRUD básicas.
 * Implementa {@link IUserService} para garantizar la implementación de métodos personalizados.
 */
@Service
public class UserService extends BaseService<User> implements IUserService {

    private final UserRepository userRepository;

    /**
     * Constructor que recibe los repositorios necesarios para operar.
     *
     * @param userRepository Repositorio específico de usuarios.
     * @param auditService   Servicio de auditoría para registrar acciones.
     */
    public UserService(UserRepository userRepository, AuditoriaService auditService) {
        super(userRepository, auditService); // Inyección en la clase base
        this.userRepository = userRepository; // Inyección específica en esta clase
    }

    /**
     * Autentica a un usuario verificando el nombre de usuario y la contraseña.
     *
     * @param username    Nombre de usuario a autenticar.
     * @param rawPassword Contraseña en texto plano (no encriptada).
     * @return Un objeto {@link UserDto} si la autenticación es exitosa, de lo contrario null.
     */
    @Override
    public UserDto authenticate(String username, String rawPassword) {
        UserDto user = userRepository.getUserWithRole(username, rawPassword);

        if (user == null) {
            // Usuario o contraseña incorrectos
            return null;
        }

        // Aquí podrías agregar una validación adicional si las contraseñas están encriptadas
        System.out.println(user.getPassword());

        return user;
    }
}
