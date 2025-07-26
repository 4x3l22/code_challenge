package com.ms.prueba.service.implement;

import com.ms.prueba.dto.CustomerDto;
import com.ms.prueba.entity.Customer;
import com.ms.prueba.repository.interfaces.BaseRepository;
import com.ms.prueba.service.interfaces.ICustomerService;
import org.springframework.stereotype.Service;

@Service
public class CustomerService extends BaseService<Customer> implements ICustomerService {

    protected CustomerService(BaseRepository<Customer, Long> repository, AuditoriaService auditService) {
        super(repository, auditService);
    }

    public void createCustomer(CustomerDto customerDto) throws Exception {
        Customer customer = new Customer();

        customer.setName(customerDto.getName());
        customer.setLastName(customerDto.getLastName());
        customer.setAge(customerDto.getAge());
        customer.setBirthDate(customerDto.getBirthDate());
        customer.setCreateAt(customerDto.getCreatedAt());
        customer.setUpdateAt(customerDto.getUpdatedAt());

        super .save(customer);
    }
}
