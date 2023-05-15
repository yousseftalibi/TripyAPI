package com.isep.trippy.Repositories;

import com.isep.trippy.Models.Traveller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
