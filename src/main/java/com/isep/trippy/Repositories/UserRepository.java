package com.isep.trippy.Repositories;
import com.isep.trippy.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class UserRepository {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    Connection connection;

    public Boolean usernameAlreadyTaken(String username) throws SQLException {
        String query = "SELECT count(username) FROM users WHERE username = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, username);
        var result = ps.executeQuery();
        return result.next();
    }

    public Optional<User> getUserById(int id) throws SQLException{
        String query = "SELECT * FROM users WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, id);
        var result = ps.executeQuery();
        RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);
        User user = result.next() ? rowMapper.mapRow(result, 0) : null;
        return Optional.of(user);
    }
    public Optional<User> getUserByUsername(String username) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, username);
        try {
            var result = ps.executeQuery();
            RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);
            var user = result.next() ? rowMapper.mapRow(result, 0) : null;
            return Optional.ofNullable(user);

        }
        catch (SQLException sqlE){
            throw new RuntimeException(sqlE);
        }

    }
    public void registerUser(User user) throws SQLException {
        String createUser = "INSERT INTO users (username, password) values (?, ?)";
        PreparedStatement ps = connection.prepareStatement(createUser);
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getPassword());
        ps.executeUpdate();
    }




}
