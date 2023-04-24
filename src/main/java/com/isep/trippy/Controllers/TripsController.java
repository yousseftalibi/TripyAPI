package com.isep.trippy.Controllers;

import com.isep.trippy.Models.Traveller;
import com.isep.trippy.Services.TripsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")

public class TripsController {

    @Autowired
    TripsService _tripsService;

    @PostMapping(value="/api/addTraveller")
    public ResponseEntity<Traveller> addTraveller(@RequestBody Traveller _traveller){
        _tripsService.addTraveller(_traveller);
        return ResponseEntity.ok(_traveller);
    }

}
