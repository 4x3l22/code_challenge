package com.ms.prueba.config;

import com.ms.prueba.dto.UserDto;
import com.ms.prueba.repository.interfaces.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Utilidad para la generación, validación y extracción de información desde tokens JWT.
 * Esta clase centraliza la lógica de seguridad basada en tokens.
 */
@Component
public class JwtUtil {

    @Autowired
    private UserRepository userRepository; // o IUserService si tienes uno


    // Clave secreta para firmar el token. Debe ser suficientemente larga y segura.
    private final String secret = "miSuperClaveSecretaJWTQueDebeSerLarga1234567890";

    // Tiempo de expiración del token en milisegundos (1 hora)
    private final long expirationMs = 1000 * 60 * 60;

    /**
     * Genera la clave de firma a partir del secreto.
     *
     * @return clave de firma HMAC-SHA.
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Genera un token JWT con el nombre de usuario y rol especificados.
     *
     * @param username Nombre del usuario.
     * @param role     Rol del usuario.
     * @return Token JWT firmado.
     */
    public String generateToken(String username, Collection<? extends GrantedAuthority> authorities) {
        // Suponiendo que lograste obtener el objeto User desde el UserDetails:
        String role = "ROLE_" + ((UserDto) userRepository.findByUsername(username)).getRol();


        System.out.println("role de prove"+ role);

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .claim("authorities", authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extrae el nombre de usuario (subject) desde el token.
     *
     * @param token Token JWT.
     * @return Nombre de usuario extraído del token.
     */
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Extrae el rol desde el token.
     *
     * @param token Token JWT.
     * @return Rol del usuario.
     */
    public String extractRole(String token) {
        return (String) getClaims(token).get("role");
    }

    /**
     * Valida si el token es correcto y no ha expirado.
     *
     * @param token Token JWT.
     * @return true si es válido, false en caso contrario.
     */
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    /**
     * Extrae las claims (información) desde el token.
     *
     * @param token Token JWT.
     * @return Claims decodificados.
     * @throws JwtException si el token es inválido.
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
