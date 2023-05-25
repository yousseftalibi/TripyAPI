package com.isep.trippy.Services;

import com.isep.trippy.Models.Place;
import com.isep.trippy.Models.User;
import com.isep.trippy.Repositories.TripsRepository;
import com.isep.trippy.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional

public class TripsService {
    @Autowired
    TripsRepository _tripsRepository;

    @Autowired
    UserRepository _userRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    Connection connection;

    public void visitPlace(Integer userId, Place place) throws SQLException {
        User omayos = _userRepository.getUserById(userId).get();
        Place place1 = Place.builder().xid("110").rate(7).kinds("monument").build();
        
        if(!_tripsRepository.placeAlreadyExists(place)) {
                    _tripsRepository.addPlaceToVisitedPlaces(place);
        } 
        _tripsRepository.addVisitedToUser(omayos, place);
    }

    public Place getPlaceById(String xid) throws SQLException {
        return _tripsRepository.getPlaceById(xid);
    }

     public List<Place> getDestinationRecommendation(Integer userId) {
        List<Place> recommendedPlaces = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        String uri = "http://localhost:8083/api/recommendDestination/"+userId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<List> response =  restTemplate.exchange(uri, HttpMethod.GET, requestEntity, List.class );
        List<String> placesIds = response.getBody();
        System.out.println(response.getBody());
       
        placesIds.forEach(place -> {
            try {
                recommendedPlaces.add(getPlaceById(place));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return recommendedPlaces;
    }
}
