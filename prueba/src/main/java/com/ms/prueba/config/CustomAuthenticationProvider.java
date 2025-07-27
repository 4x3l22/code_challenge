package com.ms.prueba.config;

import com.ms.prueba.dto.UserDto;
import com.ms.prueba.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Proveedor de autenticación personalizado que valida las credenciales
 * de un usuario usando el servicio {@link IUserService}.
 * <p>
 * Se integra con el sistema de seguridad de Spring Security para realizar
 * la autenticación a partir de nombre de usuario y contraseña.
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private IUserService userService;

    /**
     * Realiza el proceso de autenticación validando las credenciales del usuario.
     *
     * @param authentication Objeto que contiene el nombre de usuario y contraseña proporcionados por el cliente.
     * @return Un {@link UsernamePasswordAuthenticationToken} si las credenciales son válidas.
     * @throws AuthenticationException si la autenticación falla por credenciales incorrectas.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDto user = userService.authenticate(username, password);
        if (user == null) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        return new UsernamePasswordAuthenticationToken(username, password, List.of());
    }

    /**
     * Indica si este proveedor de autenticación es compatible con el tipo de autenticación proporcionado.
     *
     * @param authentication Clase del tipo de autenticación.
     * @return true si se admite {@link UsernamePasswordAuthenticationToken}, false en caso contrario.
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
