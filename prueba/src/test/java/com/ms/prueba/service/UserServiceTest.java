package com.ms.prueba.service;

import com.ms.prueba.dto.UserDto;
import com.ms.prueba.dto.UserDtoImpl;
import com.ms.prueba.repository.interfaces.UserRepository;
import com.ms.prueba.service.implement.AuditoriaService;
import com.ms.prueba.service.implement.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para {@link UserService}.
 * Se validan los casos de autenticación de usuario usando {@code UserRepository}.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuditoriaService auditoriaService;

    private UserService userService;

    /**
     * Configura el servicio y los mocks antes de cada prueba.
     */
    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, auditoriaService);
    }

    /**
     * Verifica que el método {@code authenticate} retorne un objeto {@link UserDto}
     * cuando el usuario existe en la base de datos.
     */
    @Test
    void authenticate_ShouldReturnUserDto_WhenUserExists() {
        // Arrange
        String username = "testuser";
        String password = "1234";
        UserDtoImpl mockUser = new UserDtoImpl();
        mockUser.setUsername(username);
        mockUser.setPassword(password);

        when(userRepository.getUserWithRole(username, password)).thenReturn(mockUser);

        // Act
        UserDto result = userService.authenticate(username, password);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }

    /**
     * Verifica que el método {@code authenticate} retorne {@code null}
     * cuando el usuario no es encontrado en la base de datos.
     */
    @Test
    void authenticate_ShouldReturnNull_WhenUserNotFound() {
        // Arrange
        when(userRepository.getUserWithRole("wrong", "wrong")).thenReturn(null);

        // Act
        UserDto result = userService.authenticate("wrong", "wrong");

        // Assert
        assertNull(result);
    }
}
