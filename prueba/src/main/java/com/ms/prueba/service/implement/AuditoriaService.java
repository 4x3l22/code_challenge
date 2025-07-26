package com.ms.prueba.service.implement;

import com.ms.prueba.entity.BaseEntity;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class AuditoriaService {

    public void setAuditoriaOnCreate(BaseEntity entity){
        ZonedDateTime fechaEnBogota = ZonedDateTime.now(ZoneId.of("America/Bogota"));
        entity.setCreateAt(fechaEnBogota.toLocalDateTime());
        entity.setUpdateAt(fechaEnBogota.toLocalDateTime());
        entity.setDeleteAt(null);
    }

    public void setAuditoriaOnUpdate(BaseEntity entity){
        ZonedDateTime fechaEnBogota = ZonedDateTime.now(ZoneId.of("America/Bogota"));
        entity.setUpdateAt(fechaEnBogota.toLocalDateTime());
    }

    public void setAuditoriaOnDelete(BaseEntity entity){
        ZonedDateTime fechaEnBogota = ZonedDateTime.now(ZoneId.of("America/Bogota"));
        entity.setDeleteAt(fechaEnBogota.toLocalDateTime());
    }
}
