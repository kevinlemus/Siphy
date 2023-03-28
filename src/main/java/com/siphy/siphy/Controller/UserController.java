package com.siphy.siphy.Controller;

import com.siphy.siphy.DAO.UserRepository;
import com.siphy.siphy.Model.User;
import com.siphy.siphy.Security.Password;
import com.siphy.siphy.Service.Exceptions.UserNotFoundException;
import com.siphy.siphy.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User loginRequest, HttpServletRequest request){
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword().toString();
        boolean loggedIn = userService.login(username, password, request);

        if(loggedIn){
            User user = userRepository.findById(username).get();
            String message = "You have successfully logged in";
            return ResponseEntity.ok()
                    .header("loggedIn", "Success")
                    .header("message", message)
                    .body(user);

        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<User> logout(@RequestBody User logoutRequest, HttpServletRequest request){
        userService.logout(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/registerUser")
    public ResponseEntity<User> register(@RequestBody User user){
        return new ResponseEntity<User>(userService.register(user), HttpStatus.CREATED);
    }

    @PostMapping("{username}")
    public ResponseEntity<String> delete(@PathVariable("username") String username){
        userService.delete(username);
        return new ResponseEntity<String>("Your account has been deleted", HttpStatus.OK);
    }

    @GetMapping("{username}")
    public ResponseEntity<User> getByUsername(@PathVariable("username") String username){
        try {
            return new ResponseEntity<User>(userService.getByUsername(username), HttpStatus.OK);
        }catch(UserNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getAllUsers")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @PutMapping("{username}")
    public ResponseEntity<User> updateUser(@PathVariable("username") String username, @RequestBody User user){
        return new ResponseEntity<User>(userService.updateUser(user, username), HttpStatus.OK);
    }
}
