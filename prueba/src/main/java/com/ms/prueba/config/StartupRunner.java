package com.ms.prueba.config;

import com.ms.prueba.service.implement.TokenStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private TokenStorage tokenStorage;

    @Override
    public void run(String... args) {

    }

    /**
     * Validación mínima para formato JWT (tres partes separadas por '.')
     */
    private boolean isValidJwtFormat(String token) {
        return token.split("\\.").length == 3;
    }
}
