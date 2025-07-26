package com.ms.prueba.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Filtro personalizado para interceptar las peticiones HTTP y validar el token JWT.
 * Este filtro se encarga de:
 * <ul>
 *     <li>Extraer el token JWT del encabezado Authorization.</li>
 *     <li>Validar el token utilizando {@link JwtUtil}.</li>
 *     <li>Establecer el contexto de seguridad con el usuario autenticado si el token es válido.</li>
 *     <li>Rechazar la petición si el token es inválido o si se requiere autenticación y no hay token.</li>
 * </ul>
 */
@Component
public class JwtAuthenticationFilter extends GenericFilter {

    private final JwtUtil jwtUtil;

    /**
     * Constructor para inyectar la utilidad JWT.
     *
     * @param jwtUtil instancia de utilidad para operaciones con JWT
     */
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Método principal del filtro que intercepta todas las peticiones HTTP.
     * Valida el token JWT y establece el contexto de autenticación.
     *
     * @param request  la solicitud HTTP entrante
     * @param response la respuesta HTTP
     * @param chain    la cadena de filtros
     * @throws IOException      en caso de error de entrada/salida
     * @throws ServletException en caso de error en el procesamiento del filtro
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest http = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String authHeader = http.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.replace("Bearer ", "");

            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.extractUsername(token);
                String role = jwtUtil.extractRole(token);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + role))
                        );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.getWriter().write("Token JWT inválido o expirado");
                return;
            }
        } else {
            // Si la ruta requiere autenticación y no se ha enviado token, rechaza la petición
            if (requiresAuth(http)) {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.getWriter().write("Token JWT requerido");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * Determina si una ruta específica requiere autenticación.
     * Las rutas como login, documentación Swagger y creación de usuario están excluidas.
     *
     * @param request la solicitud HTTP
     * @return true si la ruta requiere autenticación, false en caso contrario
     */
    private boolean requiresAuth(HttpServletRequest request) {
        String path = request.getRequestURI();
        return !path.startsWith("/prueba/login")
                && !path.startsWith("/prueba/swagger-token")
                && !path.startsWith("/prueba/swagger-ui")
                && !path.startsWith("/prueba/v3/api-docs")
                && !path.startsWith("/prueba/api/v1/customer/createUser");

    }

}
