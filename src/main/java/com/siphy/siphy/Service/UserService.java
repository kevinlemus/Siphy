package com.siphy.siphy.Service;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    boolean login(String username, String password, HttpServletRequest request);
    void logout(HttpServletRequest request);
    User register(User user);
    User updateUser(User user, String username);
    List<User> getAllUsers();
    User getByUsername(String username);
    void delete(String username);

}
