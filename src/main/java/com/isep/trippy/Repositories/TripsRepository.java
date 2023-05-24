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
    public void addTraveller(Traveller _traveller) throws SQLException {
        String addTraveller = "INSERT INTO Travellers (first_name, last_name, email, phone_number, nationality, city) values (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(addTraveller);
        ps.setString(1, _traveller.getFirst_name());
        ps.setString(2, _traveller.getLast_name());
        ps.setString(3, _traveller.getEmail());
        ps.setInt(4, _traveller.getPhone_number());
        ps.setString(5, _traveller.getNationality());
        ps.setString(6, _traveller.getCity());
        ps.executeUpdate();
    }

    public Array getVisitedPlaces (User user) throws SQLException{
        String getVisitedPlaces = "SELECT places FROM users where id = ?";
        PreparedStatement ps = connection.prepareStatement(getVisitedPlaces);
        ps.setInt(1, user.getId());
        var result = ps.executeQuery();
        return result.next() ? result.getArray("places") : null;
    }
    public void visitPlace(User user, Place visitedNewPlace) throws SQLException {
        Array sqlArray = getVisitedPlaces(user);
        String[] visitedPlacesArray = (String[]) sqlArray.getArray();
        List<String> visitedPlaces = new ArrayList<>(Arrays.asList(visitedPlacesArray));
        visitedPlaces.add(visitedNewPlace.xid);

        String addVisitedPlace = "UPDATE users SET places = ? WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(addVisitedPlace);

        Array updatedPlaces = connection.createArrayOf("text", visitedPlaces.toArray());
        ps.setArray(1, updatedPlaces);
        ps.setInt(2, user.getId());
        ps.executeUpdate();
    }


}
