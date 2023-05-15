package com.isep.trippy.Controllers;

import com.isep.trippy.Models.Friend;
import com.isep.trippy.Models.Traveller;
import com.isep.trippy.Models.User;
import com.isep.trippy.Services.TripsService;
import com.isep.trippy.Services.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")

public class TripsController {

    @Autowired
    TripsService _tripsService;
    @Autowired
    UserService _userService;

    @PostMapping(value="/api/addTraveller")
    public ResponseEntity<Traveller> addTraveller(@RequestBody Traveller _traveller){
       // _tripsService.addTraveller(_traveller);
        return ResponseEntity.ok(_traveller);
    }

    @GetMapping(value ="/api/getFriends/{id}")
    public ResponseEntity<List<Friend>> getFriends(@PathVariable int id) throws SQLException {
       return ResponseEntity.ok(_tripsService.suggestTrip(id));
    }

    @GetMapping(value = "/api/getUserPlaces/{id}")
    public String[] getUserPlaces(@PathVariable int id) throws SQLException {
         User user = _userService.getUser(id);
         String places = user.getPlaces();
         places = places.replace("{", "");
         places = places.replace("}", "");
         String[] array = places.split(",");
         return array;
    }

}
