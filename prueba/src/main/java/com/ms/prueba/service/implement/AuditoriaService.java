package com.ms.prueba.service.implement;

import com.ms.prueba.entity.BaseEntity;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class AuditoriaService {

    public void setAuditoriaOnCreate(BaseEntity entity){
        ZonedDateTime fechaEnBogota = ZonedDateTime.now(ZoneId.of("America/Bogota"));
        entity.setCreateAdd(fechaEnBogota.toLocalDateTime());
        entity.setUpdateAdd(fechaEnBogota.toLocalDateTime());
        entity.setDeleteAdd(null);
    }

    public void setAuditoriaOnUpdate(BaseEntity entity){
        ZonedDateTime fechaEnBogota = ZonedDateTime.now(ZoneId.of("America/Bogota"));
        entity.setUpdateAdd(fechaEnBogota.toLocalDateTime());
    }

    public void setAuditoriaOnDelete(BaseEntity entity){
        ZonedDateTime fechaEnBogota = ZonedDateTime.now(ZoneId.of("America/Bogota"));
        entity.setDeleteAdd(fechaEnBogota.toLocalDateTime());
    }
}
