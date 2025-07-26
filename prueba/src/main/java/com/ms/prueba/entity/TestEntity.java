package com.ms.prueba.entity;

import jakarta.persistence.Entity;

@Entity
public class TestEntity extends BaseEntity {
    private String name;

    public TestEntity() {}

    public TestEntity(Long id, String name) {
        this.setId(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
