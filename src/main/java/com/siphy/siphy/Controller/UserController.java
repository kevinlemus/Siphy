package com.siphy.siphy.Controller;

import com.siphy.siphy.DAO.UserRepository;
import com.siphy.siphy.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.User;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User loginRequest, HttpServletRequest request){
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        boolean loggedIn = userService.login(username, password, request);

        if(loggedIn){
            User user = userRepository.findByUsername(username);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<User> logout(@RequestBody User logoutRequest, HttpServletRequest request){
        userService.logout(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user){
        return new ResponseEntity<User>(userService.register(user), HttpStatus.CREATED);
    }

    @PostMapping("{username}")
    public ResponseEntity<String> delete(@PathVariable("username") String username){
        userService.delete(username);
        return new ResponseEntity<String>("Your account has been deleted", HttpStatus.OK);
    }
}
