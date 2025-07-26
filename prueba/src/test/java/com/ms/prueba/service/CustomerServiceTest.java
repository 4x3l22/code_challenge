package com.ms.prueba.service;

import com.ms.prueba.dto.CustomerDto;
import com.ms.prueba.dto.CustomerLifeProjection;
import com.ms.prueba.entity.Customer;
import com.ms.prueba.repository.interfaces.AgeStatsProjection;
import com.ms.prueba.repository.interfaces.BaseRepository;
import com.ms.prueba.repository.interfaces.CustomerRepository;
import com.ms.prueba.service.implement.AuditoriaService;
import com.ms.prueba.service.implement.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para {@link CustomerService}.
 * Se valida el comportamiento del servicio al gestionar clientes y sus estadísticas.
 */
class CustomerServiceTest {

    private CustomerRepository customerRepository;
    private BaseRepository<Customer, Long> baseRepository;
    private AuditoriaService auditoriaService;
    private CustomerService customerService;

    /**
     * Inicializa los mocks y la instancia del servicio antes de cada prueba.
     */
    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        baseRepository = mock(BaseRepository.class);
        auditoriaService = mock(AuditoriaService.class);
        customerService = new CustomerService(baseRepository, auditoriaService, customerRepository);
    }

    /**
     * Verifica que el método {@code createCustomer} invoque el método {@code save}
     * cuando los datos son válidos.
     */
    @Test
    void createCustomer_successful() throws Exception {
        CustomerDto dto = new CustomerDto();
        dto.setName("Juan");
        dto.setLastName("Pérez");
        dto.setAge(30);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse("2023-01-01 10:00:00", formatter);

        dto.setBirthDate(new Date());
        dto.setCreatedAt(dateTime);
        dto.setUpdatedAt(dateTime);

        CustomerService spyService = spy(customerService);
        doReturn(new Customer()).when(spyService).save(any());

        spyService.createCustomer(dto);

        verify(spyService, times(1)).save(any(Customer.class));
    }

    /**
     * Verifica que el método {@code getAgeStats} devuelva correctamente las estadísticas de edad
     * cuando el repositorio responde con datos.
     */
    @Test
    void getAgeStats_successful() {
        AgeStatsProjection mockStats = mock(AgeStatsProjection.class);
        when(customerRepository.findAgeStatistics()).thenReturn(mockStats);

        AgeStatsProjection result = customerService.getAgeStats();

        assertNotNull(result);
        assertEquals(mockStats, result);
    }

    /**
     * Verifica que el método {@code getAgeStats} lance una excepción cuando no hay estadísticas disponibles.
     */
    @Test
    void getAgeStats_throwsException_whenNull() {
        when(customerRepository.findAgeStatistics()).thenReturn(null);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> customerService.getAgeStats());

        assertEquals("No se encontraron estadísticas de edad", ex.getMessage());
    }

    /**
     * Verifica que el método {@code getCustomersWithLifeExpectancy} calcule correctamente la esperanza de vida
     * de los clientes cuando existe al menos uno registrado.
     */
    @Test
    void getCustomersWithLifeExpectancy_successful() {
        Customer customer = new Customer();
        customer.setName("Ana");
        customer.setLastName("Gómez");
        customer.setAge(40);
        customer.setBirthDate(new GregorianCalendar(1983, Calendar.JANUARY, 1).getTime());

        when(customerRepository.findAll()).thenReturn(Collections.singletonList(customer));

        List<CustomerLifeProjection> result = customerService.getCustomersWithLifeExpectancy();

        assertEquals(1, result.size());
        assertEquals("Ana", result.get(0).getName());
        assertEquals("Gómez", result.get(0).getLastName());
        assertEquals(40, result.get(0).getAge());

        LocalDate expectedDeath = LocalDate.of(1983, 1, 1).plusYears(75);
        assertEquals(expectedDeath, result.get(0).getExpectedDeathDate());
    }

    /**
     * Verifica que se lance una excepción si no existen clientes registrados
     * al invocar {@code getCustomersWithLifeExpectancy}.
     */
    @Test
    void getCustomersWithLifeExpectancy_throwsException_whenEmpty() {
        when(customerRepository.findAll()).thenReturn(Collections.emptyList());

        Exception ex = assertThrows(IllegalArgumentException.class, () -> customerService.getCustomersWithLifeExpectancy());

        assertEquals("No hay clientes registrados en el sistema", ex.getMessage());
    }
}
