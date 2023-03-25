package com.siphy.siphy.Service.Impl;

import com.siphy.siphy.DAO.UserRepository;
import com.siphy.siphy.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public boolean login(String username, String password, HttpServletRequest request) {
        User u = userRepository.findByUsername(username);
        if(u != null && passwordEncoder.matches(password, u.getPassword())){
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", u);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
        }
    }

    @Override
    public User register(User user) {
        String username = user.getUsername();
        User u = userRepository.findByUsername(username);
        if(u==null) {
            return userRepository.save(user);
        }else{
            throw new RuntimeException("Username is already taken");
        }
    }

    @Override
    public User updateUser(User user, String username) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public User getByUsername(String username) {
        return null;
    }

    @Override
    public void delete(String username) {
        userRepository.deleteById(username);
    }
}
