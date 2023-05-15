package com.isep.trippy.Controllers;

import com.isep.trippy.Models.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@CrossOrigin(origins = "http://localhost:3000")

public class UserController {
    @Autowired
    com.isep.trippy.Services.User.UserService UserService;
    @PostMapping(value="/api/registerUser")
    public ResponseEntity<Object> registerUser(@RequestBody @NotNull User user) throws SQLException {

        Boolean userAlreadyExists = UserService.usernameTaken(user.getUsername());
        return userAlreadyExists ?  ResponseEntity.status(HttpStatus.CONFLICT).build() :  ResponseEntity.ok(UserService.registerUser(user));

    }
    @PostMapping(value="/api/loginUser")
    public ResponseEntity<User> loginUser(@RequestBody @NotNull User user) throws SQLException {
        if(UserService.loginUser(user)){
            return ResponseEntity.ok(user);
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


}
