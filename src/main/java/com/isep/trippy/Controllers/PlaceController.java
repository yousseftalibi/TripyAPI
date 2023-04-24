package com.isep.trippy.Controllers;

import com.isep.trippy.Models.Place;
import com.isep.trippy.Services.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController

public class PlaceController {
    @Autowired
    PlaceService placeService;
    @PostMapping(value="/api/cleanData")
    public ResponseEntity<List<Place>> getCleanedData(@RequestBody List<Place> places){
        return ok(placeService.cleanData(places));
    }



}
