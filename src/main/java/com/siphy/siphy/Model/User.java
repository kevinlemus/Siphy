package com.siphy.siphy.Model;

import com.siphy.siphy.Security.Password;
import com.siphy.siphy.Util.UserInfo.Gender;
import com.siphy.siphy.Util.UserInfo.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.util.List;

@Entity
public class User {

    @Id
    private String username;
    private Password password;
    private Password confirmPassword;
    private List<Password> previousPasswords;
    private String firstName;
    private String lastName;
    private String email;
    private Gender gender;
    private LocalDate dateOfBirth;
    private Role role;


    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public Password getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(Password confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public List<Password> getPreviousPasswords() {
        return previousPasswords;
    }

    public void setPreviousPasswords(List<Password> previousPasswords) {
        this.previousPasswords = previousPasswords;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

}
