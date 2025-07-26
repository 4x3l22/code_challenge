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
@Component
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final TokenStorage tokenStorage;

    public JwtAuthenticationSuccessHandler(JwtUtil jwtUtil, TokenStorage tokenStorage) {
        this.jwtUtil = jwtUtil;
        this.tokenStorage = tokenStorage;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {
        String username = authentication.getName();
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER")
                .replace("ROLE_", "");

        String token = jwtUtil.generateToken(username, role);

        // Guardar token temporalmente
        tokenStorage.saveToken(token);
        System.out.println("✅ Usuario autenticado correctamente.");
        System.out.println("🔐 Token JWT generado: Bearer " + token);

        // Redirigimos al Swagger
        response.sendRedirect("/prueba/swagger-ui/index.html");
    }
}
