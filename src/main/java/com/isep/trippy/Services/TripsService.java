package com.isep.trippy.Services;

import com.isep.trippy.Models.Friend;
import com.isep.trippy.Models.Place;
import com.isep.trippy.Models.Traveller;
import com.isep.trippy.Models.User;
import com.isep.trippy.Repositories.TripsRepository;
import com.isep.trippy.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
/*
    @KafkaListener( topics="my-topic", groupId = "my-group")
    public void listen(String message){
        System.out.println("received message "+ message);;
    }

    public void addTraveller(Traveller _traveller){
        if(_traveller!=null){
            _tripsRepository.addTraveller(_traveller);
        }

    }*/


    public List<Friend> suggestTrip(int userId) throws SQLException {
        List<Friend> friends = new ArrayList<>();

        String getFriendsQuery = "SELECT * FROM friends WHERE user_id = ?";
        PreparedStatement statement = connection.prepareStatement(getFriendsQuery);
        statement.setInt(1, userId);
        ResultSet result =  statement.executeQuery();
        while (result.next()) {
            Friend friend = new Friend();
            friend.setUserId(result.getInt("user_id"));
            friend.setFriendId(result.getInt("friend_id"));
            friends.add(friend);
        }
        //get trips for each friend
        return friends;
    }

    public void addVisit() throws SQLException {
        User omayos = _userRepository.getUserById(1).get();
        Place place = Place.builder().xid("100").rate(7).kinds("monument").build();
        _tripsRepository.visitPlace(omayos, place);

        //had {9,1}
        //has to have {9, 1, 100}
        //tested & confirmed
    }

}
