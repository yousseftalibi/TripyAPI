package com.isep.trippy.Services.User;

import com.isep.trippy.Models.UserAuthInfoModel;
import com.isep.trippy.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class UserAuthInfoService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            var user = userRepository.getUserByUsername(username);
            return new UserAuthInfoModel(user.orElseThrow());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
