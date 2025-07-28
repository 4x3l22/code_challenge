package com.ms.prueba.controller;

import com.ms.prueba.dto.ApiResponseDto;
import com.ms.prueba.entity.BaseEntity;
import com.ms.prueba.service.implement.BaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class BaseController<T extends BaseEntity> {

    private final BaseService<T> baseService;

    protected BaseController(BaseService<T> baseService) {
        this.baseService = baseService;
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<T>>> all() {
        try {
            List<T> data = baseService.all();
            return ResponseEntity.ok(new ApiResponseDto<>("Datos obtenidos", data, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto<>("Error: " + e.getMessage(), null, false));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<T>> findById(@PathVariable Long id) {
        try {
            return baseService.findById(id)
                    .map(entity -> ResponseEntity.ok(new ApiResponseDto<>("Registro encontrado", entity, true)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ApiResponseDto<>("Registro no encontrado", null, false)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto<>("Error: " + e.getMessage(), null, false));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<T>> save(@RequestBody T entity) {
        try {
            T savedEntity = baseService.save(entity);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDto<>("Registro creado", savedEntity, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDto<>("Error al crear: " + e.getMessage(), null, false));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> update(@PathVariable Long id, @RequestBody T entity) {
        try {
            baseService.update(id, entity);
            return ResponseEntity.ok(new ApiResponseDto<>("Datos actualizados", null, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto<>("Error al actualizar: " + e.getMessage(), null, false));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> delete(@PathVariable Long id) {
        try {
            baseService.delete(id);
            return ResponseEntity.ok(new ApiResponseDto<>("Registro eliminado", null, true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto<>("Error al eliminar: " + e.getMessage(), null, false));
        }
    }
}

