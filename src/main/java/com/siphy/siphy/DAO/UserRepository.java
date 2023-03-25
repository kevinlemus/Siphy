package com.siphy.siphy.DAO;

import jakarta.persistence.Id;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, String> {

    @Query("select u from User u where u.username = :username")
    public User findByUsername(@Param("username") String username);
}
