package com.isep.trippy.Repositories;
import com.isep.trippy.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
@Repository
public class UserRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    PasswordEncoder passwordEncoder;

    public int usernameAlreadyTaken(String username){
        String query = "SELECT count(username) FROM users WHERE username = ?";
        return jdbcTemplate.queryForObject(query, new String[]{username}, int.class);
    }
    public void registerUser(User user) {
        String createUser = "INSERT INTO users (username, email, password) values (?, ?, ?)";
        jdbcTemplate.update( connection -> {
            PreparedStatement ps = connection.prepareStatement(createUser);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, passwordEncoder.encode(user.getPassword()));
            return ps;
        });
    }

    public User loginUser(User user)  {
        String loginUser = "SELECT username, password FROM users WHERE username = ?";
        RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);
        return jdbcTemplate.queryForObject(loginUser, rowMapper, user.getUsername());

    }



}
