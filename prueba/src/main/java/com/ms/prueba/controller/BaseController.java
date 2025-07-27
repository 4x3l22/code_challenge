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

    //    @Autowired
    private final BaseService<T> baseService;

    protected BaseController(BaseService<T> baseService) {
        this.baseService = baseService;
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<T>>>  all() throws Exception{
        try {
            return ResponseEntity.ok(new ApiResponseDto("Datos obtenidos",  baseService.all(), true));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponseDto<List<T>>(e.getMessage(), null, false));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Optional<T>>> findById(@PathVariable Long id) throws Exception{

        try {
            Optional<T> entity = baseService.findById(id);
            return ResponseEntity.ok(new ApiResponseDto<Optional<T>>("Registro encontrado", entity, true));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponseDto<Optional<T>>(e.getMessage(), null, false));
        }

    }

    @PostMapping
    public ResponseEntity<T> save(@RequestBody T entity) throws Exception{
        try {
            T saveEntity = baseService.save(entity);
            return ResponseEntity.status(HttpStatus.CREATED).body(saveEntity);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(entity);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody T entity) throws Exception{

        try {
            baseService.update(id, entity);
            return ResponseEntity.ok(new ApiResponseDto<T>("Datos actualizados", null, true));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponseDto<T>(e.getMessage(), null, false));
        }

    }


    @DeleteMapping("/{id}")
    public  ResponseEntity<ApiResponseDto<T>> delete(@PathVariable Long id) throws Exception{
        try {
            baseService.delete(id);
            return ResponseEntity.ok(new ApiResponseDto<T>("Registro eliminado", null, true));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponseDto<T>(e.getMessage(), null, false));
        }

    }
}
