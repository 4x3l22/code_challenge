package com.ms.prueba.service.interfaces;

import com.ms.prueba.entity.Customer;
import com.ms.prueba.repository.interfaces.AgeStatsProjection;

public interface ICustomerService extends IBaseService<Customer> {

    public default AgeStatsProjection getAgeStats(){
        return null;
    };
}
