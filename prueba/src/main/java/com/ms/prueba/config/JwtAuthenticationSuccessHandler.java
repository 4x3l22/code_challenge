package com.ms.prueba.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.prueba.service.implement.TokenStorage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * Manejador que se ejecuta después de una autenticación exitosa.
 * Se encarga de generar el token JWT, almacenarlo temporalmente y redirigir al usuario.
 *
 * <p>Este componente se activa cuando un usuario se autentica correctamente usando Spring Security.</p>
 */
@Component
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final TokenStorage tokenStorage;

    /**
     * Constructor que inyecta las dependencias necesarias.
     *
     * @param jwtUtil Utilidad para la generación y validación de tokens JWT.
     * @param tokenStorage Componente que permite almacenar tokens temporalmente.
     */
    public JwtAuthenticationSuccessHandler(JwtUtil jwtUtil, TokenStorage tokenStorage) {
        this.jwtUtil = jwtUtil;
        this.tokenStorage = tokenStorage;
    }

    /**
     * Método ejecutado automáticamente después de una autenticación exitosa.
     * Genera el token JWT basado en el nombre de usuario y su rol, lo guarda
     * temporalmente y redirige al usuario al Swagger UI.
     *
     * @param request objeto {@link HttpServletRequest} con los detalles de la solicitud.
     * @param response objeto {@link HttpServletResponse} donde se escribe la redirección.
     * @param authentication objeto {@link Authentication} con los datos del usuario autenticado.
     * @throws IOException si ocurre un error durante la escritura de la respuesta.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {

        // Obtener el nombre de usuario autenticado
        String username = authentication.getName();

        // Obtener el rol del usuario (ej. ROLE_ADMIN → ADMIN)
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER")
                .replace("ROLE_", "");

        // Generar el token JWT
        String token = jwtUtil.generateToken(username, role);

        // Almacenar temporalmente el token (por ejemplo, en memoria)
        tokenStorage.saveToken(token);

        // Logs informativos (opcional)
        System.out.println("✅ Usuario autenticado correctamente.");
        System.out.println("🔐 Token JWT generado: Bearer " + token);

        // Redirigir al usuario a la interfaz Swagger
        response.sendRedirect("/prueba/swagger-ui/index.html");
    }
}
