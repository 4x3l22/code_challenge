package com.ms.prueba.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * Clase de configuración de seguridad para Spring Security.
 * Se encarga de definir los filtros, rutas públicas y protegidas,
 * así como el proveedor de autenticación personalizado y el manejo de tokens JWT.
 */
@Configuration
@EnableWebSecurity
@EnableCaching // Habilita el uso de caché en toda la aplicación
public class SecurityConfig {

    /**
     * Define la cadena de filtros de seguridad de Spring Security.
     *
     * @param http            objeto HttpSecurity para configurar las reglas de seguridad
     * @param authProvider    proveedor de autenticación personalizado
     * @param successHandler  manejador de autenticación exitosa para el login
     * @param jwtFilter       filtro personalizado para validar tokens JWT
     * @return la cadena de filtros configurada
     * @throws Exception en caso de error en la configuración
     */
    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            CustomAuthenticationProvider authProvider,
            JwtAuthenticationSuccessHandler successHandler,
            JwtAuthenticationFilter jwtFilter
    ) throws Exception {
        http
                // Deshabilita la protección CSRF para simplificar pruebas y desarrollo
                .csrf(csrf -> csrf.disable())

                // Se configura el proveedor de autenticación personalizado
                .authenticationProvider(authProvider)

                // Se agrega el filtro de JWT antes del filtro estándar de autenticación
                .addFilterBefore(jwtFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)

                // Configuración de las reglas de autorización
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos que no requieren autenticación
                        .requestMatchers(
                                "/login",
                                "/css/**",
                                "/js/**",
                                "/api/v1/customer/createUser",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                       // .requestMatchers("/swagger-token").permitAll()

                        // Reglas basadas en roles para los diferentes métodos HTTP
                        .requestMatchers(HttpMethod.GET, "/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")

                        // Cualquier otra petición requiere autenticación
                        .anyRequest().authenticated()
                )

                // Configuración del login por formulario
                .formLogin(form -> form
                        .successHandler(successHandler) // Manejador personalizado al autenticarse correctamente
                        .permitAll()
                )

                // Configuración del logout
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout") // Redirige al login tras cerrar sesión
                        .permitAll()
                );

        return http.build(); // Retorna el objeto de configuración final
    }

    /**
     * Configura y expone el AuthenticationManager a partir del AuthenticationConfiguration.
     *
     * @param config configuración de autenticación automática de Spring
     * @return instancia de AuthenticationManager
     * @throws Exception si falla la inicialización
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
