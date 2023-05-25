package com.isep.trippy.Controllers;


import com.isep.trippy.Models.Place;
import com.isep.trippy.Models.User;
import com.isep.trippy.Services.TripsService;
import com.isep.trippy.Services.User.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping(value = "/api/getUserPlaces/{id}")
    public String[] getUserPlaces(@PathVariable int id) throws SQLException {
         User user = _userService.getUserById(id);
         String places = user.getPlaces();
         places = places.replace("{", "");
         places = places.replace("}", "");
         String[] array = places.split(",");
         return array;
    }

    @PostMapping(value="/api/visitPlace")
    public void visitPlace(@RequestParam @NotNull Integer userId, @RequestBody @NotNull Place place) throws SQLException {
        _tripsService.visitPlace(userId, place);
    }

    @GetMapping(value="/api/recommendTrips/{id}")
    public List<Place> recommendTrips(@PathVariable int id)  {
        return _tripsService.getDestinationRecommendation(id);
    }


    
}
