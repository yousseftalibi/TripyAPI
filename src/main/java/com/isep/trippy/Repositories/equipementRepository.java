package com.isep.trippy.Repositories;
import com.isep.trippy.Models.car;
import com.isep.trippy.Models.homeAppliance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
@Repository
public class equipementRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public void registerCar(car car){
        String addCar = "INSERT INTO cars (engineType, drivingFrequency, carAge) values (?, ?, ?)";
        jdbcTemplate.update( connection -> {
            PreparedStatement ps = connection.prepareStatement(addCar);
            ps.setString(1, car.engineType);
            ps.setInt(2, car.drivingFrequency);
            ps.setInt(3, car.carAge);
            return ps;
        });
    }

    public void registerHomeAppliance(homeAppliance homeAppliance){
        String addHomeAppliance = "INSERT INTO home_appliance (type, frequency, energy_rating) values (?, ?, ?)";
        jdbcTemplate.update( connection -> {
            PreparedStatement ps = connection.prepareStatement(addHomeAppliance);
            ps.setString(1, homeAppliance.type);
            ps.setInt(2, homeAppliance.frequency);
            ps.setInt(3, homeAppliance.energy_rating);
            return ps;
        });
    }
}
