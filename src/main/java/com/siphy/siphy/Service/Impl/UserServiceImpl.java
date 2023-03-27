package com.siphy.siphy.Service.Impl;

import com.siphy.siphy.DAO.UserRepository;
import com.siphy.siphy.Model.User;
import com.siphy.siphy.Security.Password;
import com.siphy.siphy.Service.Exceptions.*;
import com.siphy.siphy.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
            throw new InvalidCredentialsException("The provided credentials were incorrect.");
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
        Optional<User> optionalUser = userRepository.findById(username);
        if(optionalUser.isPresent()){
            User existingUser = optionalUser.get();
            if(user.getUsername() != null && !user.getUsername().equals(existingUser.getUsername())){
                if(userRepository.existsByUsername(user.getUsername())){
                    throw new UsernameAlreadyExists("Username is already taken.");
                }
                existingUser.setUsername(user.getUsername());
            }
            if(user.getPassword() != null && !user.getPassword().equals(existingUser.getPassword())){
                if(user.getConfirmPassword() == null || !user.getConfirmPassword().equals(user.getPassword())){
                    throw new PasswordMismatchException("Passwords do not match.");
                }
                if(existingUser.getPreviousPasswords().stream().anyMatch(p -> passwordEncoder.matches(user.getPassword(), String.valueOf(p)))){
                    throw new InvalidPasswordException("Password has already been used.");
                }
                Password newPassword = new Password(passwordEncoder.encode(user.getPassword()), LocalDate.now());
                existingUser.getPreviousPasswords().add(newPassword);
                existingUser.setPassword(newPassword.getHashedPassword());
            }
            if(user.getFirstName() != null){
                existingUser.setFirstName(user.getFirstName());
            }
            if(user.getLastName() != null){
                existingUser.setLastName(user.getLastName());
            }
            if(user.getEmail() != null){
                existingUser.setEmail(user.getEmail());
            }
            if(user.getGender() != null){
                existingUser.setGender(user.getGender());
            }
            if(user.getDateOfBirth() != null){
                existingUser.setDateOfBirth(user.getDateOfBirth());
            }

            return userRepository.save(existingUser);
        }else{
            throw new UserNotFoundException("No user with the username "+username+" was found.");
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        if(allUsers.isEmpty()) {
            throw new UserNotFoundException("No users were found.");
        }
            return allUsers;
    }

    @Override
    public User getByUsername(String username) {
        Optional<User> user = userRepository.findById(username);
        if(user.isPresent()){
            return user.get();
        }else{
            throw new UserNotFoundException("There is no existing account with the username "+username+".");
        }
    }

    @Override
    public void delete(String username) {
        userRepository.deleteById(username);
    }
}
