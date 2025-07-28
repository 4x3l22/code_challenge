package com.ms.prueba.service.implement;

import com.ms.prueba.entity.BaseEntity;
import com.ms.prueba.repository.interfaces.BaseRepository;
import com.ms.prueba.service.interfaces.IBaseService;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class BaseService<T extends BaseEntity> implements IBaseService<T> {

    protected final BaseRepository<T, Long> repository;
    protected final AuditoriaService auditService;

    protected BaseService(BaseRepository<T, Long> repository, AuditoriaService auditService) {
        this.repository = repository;
        this.auditService = auditService;
    }

    /**
     * Devuelve todas las entidades activas (con fecha de creación válida).
     */
    @Override
    public List<T> all() throws Exception {
        List<T> entities = repository.findAll();
        List<T> result = new ArrayList<>();

        for (T item : entities) {
            if (item.getCreateAt() != null && item.getDeleteAt() == null) {
                result.add(item);
            }
        }

        return result;
    }

    /**
     * Busca una entidad por su ID.
     */
    @Override
    public Optional<T> findById(Long id) throws Exception {
        return repository.findById(id);
    }

    /**
     * Guarda una nueva entidad o actualiza una existente, aplicando auditoría.
     */
    @Override
    public T save(T entity) throws Exception {
        if (entity.getCreateAt() == null) {
            auditService.setAuditoriaOnCreate(entity);
        } else {
            auditService.setAuditoriaOnUpdate(entity);
        }
        return repository.save(entity);
    }

    /**
     * Actualiza una entidad existente por ID, ignorando ciertos campos.
     */
    @Override
    public void update(Long id, T entity) throws Exception {
        Optional<T> optionalT = repository.findById(id);
        if (optionalT.isEmpty()) {
            throw new Exception("No se encontró el registro con ID: " + id);
        }

        T objetoToUpdate = optionalT.get();

        // Ignorar propiedades que no deben sobrescribirse
        String[] ignoreProperties = { "id", "createAt", "deleteAt" };
        BeanUtils.copyProperties(entity, objetoToUpdate, ignoreProperties);

        auditService.setAuditoriaOnUpdate(objetoToUpdate);
        repository.save(objetoToUpdate);
    }

    /**
     * Elimina (lógicamente) una entidad por ID.
     */
    @Override
    public void delete(Long id) throws Exception {
        Optional<T> optionalT = repository.findById(id);
        if (optionalT.isEmpty()) {
            throw new Exception("No se encontró el registro con ID: " + id);
        }

        T objetoToDelete = optionalT.get();
        auditService.setAuditoriaOnDelete(objetoToDelete);
        repository.save(objetoToDelete); // Guardar cambios en campo deleteAt
    }
}

