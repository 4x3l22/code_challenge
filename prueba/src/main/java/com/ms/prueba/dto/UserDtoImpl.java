package com.ms.prueba.dto;

public class UserDtoImpl implements UserDto {
    private String username;
    private String password;
    private String rol;

    public UserDtoImpl() {}

    public UserDtoImpl(String username, String password, String rol) {
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getRol() {
        return rol;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}