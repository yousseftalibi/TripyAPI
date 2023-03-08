package com.isep.trippy.Controllers;

import com.isep.trippy.Models.car;
import com.isep.trippy.Models.homeAppliance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")

public class equipementController {
    @Autowired
    com.isep.trippy.Services.equipementService equipementService;

    @PostMapping(value="/api/registerEquipement/car")
    public ResponseEntity<car> registerCar(@RequestBody car car){
        System.out.println(car.carAge + " " + car.engineType);
        equipementService.registerCar(car);
        return ResponseEntity.ok(car);
    }

    @PostMapping(value="/api/registerEquipement/homeAppliance")
    public ResponseEntity<homeAppliance> registerHomeAppliance(@RequestBody homeAppliance homeAppliance){
        System.out.println(homeAppliance.type+ " " + homeAppliance.frequency);
        equipementService.registerHomeAppliance(homeAppliance);
        return ResponseEntity.ok(homeAppliance);
    }

}
