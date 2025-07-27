package com.ms.prueba.repository.interfaces;

import com.ms.prueba.dto.UserDto;
import com.ms.prueba.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface UserRepository extends BaseRepository<User, Long> {

        @Query(value = "SELECT " +
                "u.username AS username, " +
                "u.password AS password, " +
                "u.rol AS rol " +
                "FROM user u " +
                "WHERE u.username = :username AND u.password = :password",
                nativeQuery = true)
        UserDto getUserWithRole(@Param("username") String username, @Param("password") String password);
        @Query(value = "SELECT " +
                "u.username AS username, " +
                "u.password AS password, " +
                "u.rol AS rol " +
                "FROM user u " +
                "WHERE u.username = :username ",
                nativeQuery = true)
        UserDto findByUsername(@Param("username") String username);
}
