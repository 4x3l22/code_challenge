package com.ms.prueba.service.implement;

import com.ms.prueba.dto.CustomerDto;
import com.ms.prueba.dto.CustomerLifeProjection;
import com.ms.prueba.entity.Customer;
import com.ms.prueba.repository.interfaces.AgeStatsProjection;
import com.ms.prueba.repository.interfaces.BaseRepository;
import com.ms.prueba.repository.interfaces.CustomerRepository;
import com.ms.prueba.service.interfaces.ICustomerService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService extends BaseService<Customer> implements ICustomerService {

    private CustomerRepository customerRepository;
    private static final int LIFE_EXPECTANCY_YEARS = 75;

    protected CustomerService(BaseRepository<Customer, Long> repository, AuditoriaService auditService, CustomerRepository customerRepository) {
        super(repository, auditService);
        this.customerRepository = customerRepository;
    }

    public void createCustomer(CustomerDto customerDto) throws Exception {
        if (customerDto == null) {
            throw new IllegalArgumentException("El cuerpo del cliente no puede estar vacío");
        }

        Customer customer = new Customer();

        customer.setName(customerDto.getName());
        customer.setLastName(customerDto.getLastName());
        customer.setAge(customerDto.getAge());
        customer.setBirthDate(customerDto.getBirthDate());
        customer.setCreateAt(customerDto.getCreatedAt());
        customer.setUpdateAt(customerDto.getUpdatedAt());

        super.save(customer);
    }


    public AgeStatsProjection getAgeStats() {
        AgeStatsProjection stats = customerRepository.findAgeStatistics();
        if (stats == null) {
            throw new IllegalArgumentException("No se encontraron estadísticas de edad");
        }
        return stats;
    }

    public List<CustomerLifeProjection> getCustomersWithLifeExpectancy() {
        List<Customer> customers = customerRepository.findAll();

        if (customers == null || customers.isEmpty()) {
            throw new IllegalArgumentException("No hay clientes registrados en el sistema");
        }

        return customers.stream().map(customer -> {
            LocalDate birthDate = customer.getBirthDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            LocalDate expectedDeathDate = birthDate.plusYears(LIFE_EXPECTANCY_YEARS);

            return new CustomerLifeProjection(
                    customer.getName(),
                    customer.getLastName(),
                    customer.getAge(),
                    birthDate,
                    expectedDeathDate
            );
        }).collect(Collectors.toList());
    }
}
