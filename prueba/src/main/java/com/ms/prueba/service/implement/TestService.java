package com.ms.prueba.service.implement;

import com.ms.prueba.entity.TestEntity;
import com.ms.prueba.repository.interfaces.TestRepository;

public class TestService extends BaseService<TestEntity> {
    public TestService(TestRepository repository, AuditoriaService auditService) {
        super(repository, auditService);
    }
}