package com.ms.prueba.repository.interfaces;

import com.ms.prueba.entity.Customer;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends BaseRepository<Customer, Long>{

    @Query(value = """
    SELECT 
        AVG(age) AS average,
        STDDEV(age) AS stddev
    FROM clientes
""", nativeQuery = true)
    AgeStatsProjection findAgeStatistics();

}
