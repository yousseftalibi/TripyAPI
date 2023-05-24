package com.isep.dataengineservice.Controllers;

import com.isep.dataengineservice.Models.Place;
import com.isep.dataengineservice.Models.User;
import com.isep.dataengineservice.Services.GeoNodeService;
import com.isep.dataengineservice.Services.PlaceClusteringService;
import com.isep.dataengineservice.Services.PlaceService;
import com.isep.dataengineservice.Services.UserService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class PlaceController {
    @Autowired
    PlaceService placeService;
    @Autowired
    PlaceClusteringService placeClusteringService;
    @Autowired
    GeoNodeService geoNodeService;
    @Autowired
    KafkaTemplate<String, List<Place>> kafkaPlaceTemplate;
    @Autowired
    UserService userService;
    @PostMapping(value="/api/getFriends")
    public List<User> getFriends() throws SQLException {
        User omayos = userService.getUserById(1);
        return userService.getFriends(omayos);
    }
    @GetMapping(value="/api/rawPlaces")
    public List<Place> rawPlaces(@RequestParam Double lon, Double lat ){
        List<Place> rawPlacesFromPosition = placeService.getRawPlaces(lon, lat);
        return placeService.getRawPlaces(lon, lat);
    }

    //gets clusteredPlaces from rawPlaces
  /*  @KafkaListener(topics= "rawPlaces", groupId = "new-places-group", containerFactory = "placeListListenerContainerFactory")
    public void rawPlacesListener(@NotNull ConsumerRecord<String, List<Place>> record){
        System.out.println("received rawPlaces from GeoNodeController.");
        List<Place> rawPlacesFromPosition = record.value().stream().collect(Collectors.toList());
        List<Place> interestingPlaces = new ArrayList<>();
        if(!rawPlacesFromPosition.isEmpty()) {
           interestingPlaces = placeClusteringService.DbscanCluster(rawPlacesFromPosition).get();
            System.out.println("got interesting places from clusterPlaces() method.");
        }
        if(!interestingPlaces.isEmpty()) {
            System.out.println("sending interesting places to interestingPlacesListener.");
            kafkaPlaceTemplate.send("interestingPlaces", interestingPlaces);
        }
    }*/

    //prints clusteredPlaces from clusteredPlaces --normally we would store in database--
    @KafkaListener(topics="interestingPlaces", groupId = "places-group", containerFactory = "placeListListenerContainerFactory")
    public void interestingPlacesListener(@NotNull ConsumerRecord<String, List<Place>> record) throws IOException {
        System.out.println("received interesetingPlaces from rawPlacesListener in PlaceController");
        List<Place> interestingPlaces = record.value();

        if(!interestingPlaces.isEmpty()) {
            File resultFile = new File("C:\\Users\\youss\\OneDrive\\Desktop\\desktop\\result"+record.offset()+".txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile.getAbsoluteFile()));
            System.out.println("processing interesting places.");
            System.out.println("Interesting Places");
            interestingPlaces.forEach(clusteredPlace -> {
                try {
                    writer.write(clusteredPlace.getName() + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            writer.close();
        }
    }

    @GetMapping(value="/api/predictPlaces")
    public List<Place> predictPlace() throws SQLException  {
        List<Place> places = userService.predictPlace(userService.getUserById(17));
        return places;
    }

    //cron job getting geoPosition
    //once gotten, kafka listenner that gets raw Places in each
    //once done, kafka listener that clusters list



    /*what airflow will do
        GeoPosition geoPosition = geoPositions("Paris");
        List<Place> rawPlaces = rawPlaces( geoPosition.getLon(), geoPosition.getLat());
        List<Place> formattedPlaces = formattedPlaces(rawPlaces);
*/
    //use Kafka to run this over&over again in perimeters different than 500

    //controller to store the data in the data lake
    //controller to get tweets
    //controller to clean tweets
    //controller to store tweets
    //controller to combine places & tweets
    //controller to return sentiment analysis
    //controller to store sentiment analysis in data lake
    //controller to index in elastic

}
