package com.isep.dataengineservice.Repository.User;

import com.isep.dataengineservice.Models.User.ChatMessage;
import com.isep.dataengineservice.Models.User.Profile;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProfileRepository {
    @Autowired
    Connection connection;

    public Profile getProfileByUserId(int userId) throws SQLException {
        String query = "SELECT * FROM profile WHERE user_id = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, userId);
        var result = ps.executeQuery();
        RowMapper<Profile> rowMapper = new BeanPropertyRowMapper<>(Profile.class);
        return result.next() ? rowMapper.mapRow(result, 0) : null;
    }

    public void addOrUpdateProfile(Profile profile) throws SQLException {
        // first try to update existing profile
        String updateQuery = "UPDATE profile SET first_name = ?, last_name = ?, email_address = ?, nationality = ? WHERE user_id = ?";
        PreparedStatement updatePs = connection.prepareStatement(updateQuery);
        updatePs.setString(1, profile.getFirstName());
        updatePs.setString(2, profile.getLastName());
        updatePs.setString(3, profile.getEmailAddress());
        updatePs.setString(4, profile.getNationality());
        updatePs.setInt(5, profile.getUser().getId());
        int affectedRows = updatePs.executeUpdate();

        // if no rows were affected, the profile doesn't exist, so we try to insert
        if (affectedRows == 0) {
            String insertQuery = "INSERT INTO profile (user_id, first_name, last_name, email_address, nationality) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertPs = connection.prepareStatement(insertQuery);
            insertPs.setInt(1, profile.getUser().getId());
            insertPs.setString(2, profile.getFirstName());
            insertPs.setString(3, profile.getLastName());
            insertPs.setString(4, profile.getEmailAddress());
            insertPs.setString(5, profile.getNationality());
            insertPs.executeUpdate();
        }
    }

    public void saveMessage(ChatMessage chatMessage) {
        try {
            String insertQuery = "INSERT INTO messages (user_id, message) VALUES (?, ?)";
            PreparedStatement insertPs = connection.prepareStatement(insertQuery);
            insertPs.setInt(1, chatMessage.getId() );
            insertPs.setString(2, chatMessage.getMessage());
            insertPs.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ChatMessage> getMessagesByUserId(int userId) throws SQLException {
        String query = "SELECT * FROM messages WHERE user_id = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, userId);
        var result = ps.executeQuery();
        RowMapper<ChatMessage> rowMapper = new BeanPropertyRowMapper<>(ChatMessage.class);
        List<ChatMessage> messages = new ArrayList<>();
        while (result.next()) {
            messages.add(rowMapper.mapRow(result, 0));
        }
        return messages;
    }
}
