package com.isep.trippy.Controllers;

import com.isep.trippy.Services.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;


@RestController

public class PlaceController {
    @Autowired
    PlaceService placeService;
    @Autowired
    KafkaTemplate kafkaTemplate;


}
