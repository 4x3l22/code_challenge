package com.ms.prueba.service.implement;

import com.ms.prueba.entity.Customer;
import com.ms.prueba.repository.interfaces.BaseRepository;
import com.ms.prueba.service.interfaces.ICustomerService;
import org.springframework.stereotype.Service;

@Service
public class CustomerService extends BaseService<Customer> implements ICustomerService {

    protected CustomerService(BaseRepository<Customer, Long> repository, AuditoriaService auditService) {
        super(repository, auditService);
    }
}
