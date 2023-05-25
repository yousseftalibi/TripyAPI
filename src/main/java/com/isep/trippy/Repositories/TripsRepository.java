package com.isep.trippy.Repositories;

import com.isep.trippy.Models.Place;
import com.isep.trippy.Models.Traveller;
import com.isep.trippy.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Repository
public class TripsRepository {

    @Autowired
    Connection connection;

    public Array getVisitedPlaces (User user) throws SQLException{
        String getVisitedPlaces = "SELECT places FROM users where id = ?";
        PreparedStatement ps = connection.prepareStatement(getVisitedPlaces);
        ps.setInt(1, user.getId());
        var result = ps.executeQuery();
        return result.next() ? result.getArray("places") : null;
    }
    public void addVisitedToUser(User user, Place visitedNewPlace) throws SQLException {
        Array sqlArray = getVisitedPlaces(user);
        String[] visitedPlacesArray = (String[]) sqlArray.getArray();
        List<String> visitedPlaces = new ArrayList<>(Arrays.asList(visitedPlacesArray));

        if(!visitedPlaces.contains(visitedNewPlace.getXid())){
            visitedPlaces.add(visitedNewPlace.xid);
            String addVisitedPlaceToUser = "UPDATE users SET places = ? WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(addVisitedPlaceToUser);
            Array updatedPlaces = connection.createArrayOf("text", visitedPlaces.toArray());
            ps.setArray(1, updatedPlaces);
            ps.setInt(2, user.getId());
            ps.executeUpdate();
        }
    }

      public Place getPlaceById(String xid) throws SQLException {
        String getPlaceByIdQuery = "SELECT * FROM places WHERE xid = ?";
        PreparedStatement ps = connection.prepareStatement(getPlaceByIdQuery);
        ps.setString(1, xid);
        var result = ps.executeQuery();
        if(result.next()){
            return Place.builder().xid(result.getString("xid")).rate(result.getInt("rate")).kinds(result.getString("kinds")).dist(result.getDouble("dist")).name(result.getString("name")).build();
        }   
        return null;
    }

    public Boolean placeAlreadyExists (Place place) throws SQLException {
        String placeExistsQuery = "SELECT * FROM places WHERE xid = ?";
        PreparedStatement ps = connection.prepareStatement(placeExistsQuery);
        ps.setString(1, place.xid);
        var result = ps.executeQuery();
       return result.next();
    }
    
    public void addPlaceToVisitedPlaces (Place place) throws SQLException {
        String visitPlaceQuery = "INSERT INTO places (xid, rate, kinds, dist, name) values (?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(visitPlaceQuery);
        ps.setString(1, place.xid);
        ps.setInt(2, place.rate);
        ps.setString(3, place.kinds);
        ps.setDouble(4, place.dist);
        ps.setString(5, place.name);
        ps.executeUpdate();
    }



}
