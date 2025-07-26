package com.ms.prueba.service.implement;

import com.ms.prueba.entity.User;
import com.ms.prueba.repository.interfaces.BaseRepository;
import com.ms.prueba.service.interfaces.IUserService;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService<User> implements IUserService {

    protected UserService(BaseRepository<User, Long> repository, AuditoriaService auditService) {
        super(repository, auditService);
    }
}
