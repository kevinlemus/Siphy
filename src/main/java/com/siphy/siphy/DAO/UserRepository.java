package com.siphy.siphy.DAO;

import com.siphy.siphy.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, String> {

    @Query("select u from User u where u.username = :username")
    public User findByUsername(@Param("username") String username);
    @Query("select case count(u) > 0 then true else false end " +
            "from User u where u.username = :username")
    boolean existsByUsername(@Param("username") String username);

}
