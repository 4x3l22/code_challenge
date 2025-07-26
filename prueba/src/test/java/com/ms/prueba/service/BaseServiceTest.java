package com.ms.prueba.service;

import com.ms.prueba.entity.TestEntity;
import com.ms.prueba.repository.interfaces.TestRepository;
import com.ms.prueba.service.implement.AuditoriaService;
import com.ms.prueba.service.implement.TestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para {@link TestService}, utilizando JUnit y Mockito.
 * Se valida el comportamiento de los métodos de negocio asociados a TestEntity.
 */
public class BaseServiceTest {

    @Mock
    private TestRepository testRepository;

    @Mock
    private AuditoriaService auditoriaService;

    @InjectMocks
    private TestService testService;

    /**
     * Inicializa los mocks antes de cada prueba.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Verifica que el método {@code all()} solo retorne entidades cuya fecha de creación {@code createAt} sea nula.
     */
    @Test
    public void all_ShouldReturnOnlyItemsWithNullCreateAt() throws Exception {
        TestEntity e1 = new TestEntity(1L, "A");
        e1.setCreateAt(null);

        TestEntity e2 = new TestEntity(2L, "B");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse("2023-01-01 10:00:00", formatter);
        e2.setCreateAt(dateTime);

        when(testRepository.findAll()).thenReturn(List.of(e1, e2));

        List<TestEntity> result = testService.all();

        assertEquals(1, result.size());
        assertEquals("A", result.get(0).getName());
    }

    /**
     * Verifica que al guardar una entidad sin fecha de creación, se invoque la auditoría
     * de creación antes de persistirla.
     */
    @Test
    public void save_ShouldCallAuditCreate_WhenCreateAtIsNull() throws Exception {
        TestEntity entity = new TestEntity(1L, "New");
        entity.setCreateAt(null);

        when(testRepository.save(entity)).thenReturn(entity);

        TestEntity saved = testService.save(entity);

        verify(auditoriaService).setAuditoriaOnCreate(entity);
        verify(testRepository).save(entity);
        assertEquals("New", saved.getName());
    }

    /**
     * Verifica que el método {@code findById} devuelva la entidad correspondiente si se encuentra en la base de datos.
     */
    @Test
    public void findById_ShouldReturnEntity_WhenFound() throws Exception {
        TestEntity entity = new TestEntity(1L, "Existing");

        when(testRepository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<TestEntity> result = testService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Existing", result.get().getName());
    }
}
