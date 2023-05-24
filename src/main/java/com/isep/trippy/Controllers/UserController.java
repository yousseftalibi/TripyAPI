package com.isep.trippy.Controllers;

import com.isep.trippy.Models.User;
import com.isep.trippy.Services.User.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")

public class UserController {
    @Autowired
    UserService userService;
    @PostMapping(value="/api/registerUser")
    public ResponseEntity<Object> registerUser(@RequestBody @NotNull User user) throws SQLException {

        Boolean userAlreadyExists = userService.usernameTaken(user.getUsername());
        return userAlreadyExists ?  ResponseEntity.status(HttpStatus.CONFLICT).build() :  ResponseEntity.ok(userService.registerUser(user));

    }
    @PostMapping(value="/api/loginUser")
    public ResponseEntity<User> loginUser(@RequestBody @NotNull User user) throws SQLException {
        if(userService.loginUser(user)){
            return ResponseEntity.ok(user);
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping(value="/api/getFriends")
    public List<User> getFriends() throws SQLException {
        User omayos = userService.getUserById(1);
        return userService.getFriends(omayos);
    }
}
