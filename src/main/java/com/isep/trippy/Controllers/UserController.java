package com.isep.trippy.Controllers;

import com.isep.trippy.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")

public class UserController {
    @Autowired
    com.isep.trippy.Services.UserService UserService;
    @PostMapping(value="/api/registerUser")
    public ResponseEntity<Object> registerUser(@RequestBody User user)  {
        if(UserService.usernameTaken(user.getUsername())){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        UserService.registerUser(user);
        return ResponseEntity.ok(user);
    }
    @PostMapping(value="/api/loginUser")
    public ResponseEntity<User> loginUser(@RequestBody User user) {
        if(UserService.loginUser(user)){
            return ResponseEntity.ok(user);
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


}
