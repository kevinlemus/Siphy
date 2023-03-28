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
import java.util.ArrayList;
import java.util.Arrays;
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
        if(u != null && passwordEncoder.matches(password, u.getPassword().toString())){
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
        if(u!=null) {
            throw new RuntimeException("Username is already taken");
        }
        if(user.getUsername().isEmpty()){
            throw new RuntimeException("Username must not be empty");
        }
        if(user.getFirstName().isEmpty()){
            throw new RuntimeException("First name cannot be empty");
        }
        if(user.getLastName().isEmpty()){
            throw new RuntimeException("Last name cannot be empty");
        }
        if(user.getGender() == null){
            throw new RuntimeException("Must select a gender option");
        }

        String regex = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$){8,}";
        String missingRequirements = "";
        List<String> requirements = Arrays.asList(
                "At least 8 characters long",
                "Contains at least one digit",
                "Contains at least one lowercase letter",
                "Contains at least one uppercase letter",
                "Contains at least one special character (@#$%^&+=)",
                "Cannot have empty spaces"
        );


        String stringPassword = user.getPassword().toString();
        if(stringPassword.length()<8){
            missingRequirements += requirements.get(0) + ", ";
        }
        if(stringPassword.matches(".*[0-9]")){
            missingRequirements += requirements.get(1) + ", ";
        }
        if(stringPassword.matches(".*[a-z]")){
            missingRequirements += requirements.get(2) + ", ";
        }
        if(stringPassword.matches(".*[A-Z]")){
            missingRequirements += requirements.get(3) + ", ";
        }
        if(stringPassword.matches(".*[@#$%^&+=]")){
            missingRequirements += requirements.get(4) + ", ";
        }
        if(stringPassword.contains(" ")){
            missingRequirements += requirements.get(5) + ", ";
        }


        if(stringPassword.matches(regex)){
            throw new InvalidPasswordException("Password does not meet the following requirements: "+missingRequirements);
        }
        if(!user.getPassword().equals(user.getConfirmPassword())){
            throw new PasswordMismatchException("Passwords do not match.");
        }
        if(user.getDateOfBirth() == null){
            throw new RuntimeException("Must enter your date of birth");
        }
        if(user.getDateOfBirth() != null && user.getDateOfBirth().isAfter(LocalDate.now())){
            throw new RuntimeException("Date of birth cannot be in the future.");
        }

        Password hashedPassword = new Password(passwordEncoder.encode(user.getPassword().toString()));
        user.setPassword(hashedPassword);
        user.getPreviousPasswords().add(hashedPassword);

        return userRepository.save(user);

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
                if(existingUser.getPreviousPasswords().stream().anyMatch(p -> passwordEncoder.matches(user.getPassword().toString(), String.valueOf(p)))){
                    throw new InvalidPasswordException("Password has already been used.");
                }
                Password oldPassword = existingUser.getPassword();//creating a way to change properties of our old password
                oldPassword.setDateLastUsed(LocalDate.now());//we are setting the last date it was used before changing the password
                Password newPassword = new Password(passwordEncoder.encode(user.getPassword().toString()));//created a password instance with and set the hashed password in the constructor
                existingUser.getPreviousPasswords().add(newPassword);//adding the new password to all passwords the user has used.
                existingUser.setPassword(newPassword);//changing password to the new password
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
