package com.ms.prueba.repository.interfaces;

import com.ms.prueba.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseRepository<T extends BaseEntity, id> extends JpaRepository<T, Long> {
}
