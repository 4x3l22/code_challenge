package com.ms.prueba.service.implement;


import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Component
public class TokenStorage {

    private final Path tokenFilePath = Paths.get(System.getProperty("java.io.tmpdir"), "jwt-token.txt");

    public void saveToken(String token) {
        try {
            Files.writeString(tokenFilePath, token, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readToken() {
        try {
            return Files.readString(tokenFilePath);
        } catch (IOException e) {
            return null;
        }
    }

    public void deleteToken() {
        try {
            Files.deleteIfExists(tokenFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}