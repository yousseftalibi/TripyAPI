package com.isep.trippy.Services;
import com.isep.trippy.Models.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.isep.trippy.Repositories.UserRepository;
@Service
@Transactional
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    public void registerUser(User user)  {
        if(user !=null){
            userRepository.registerUser(user);
        }
    }

    public Boolean loginUser(User user) {
            User existingUser = userRepository.loginUser(user);
            return existingUser!=null && (passwordEncoder.matches(user.getPassword(), existingUser.getPassword()));

    }

    public boolean usernameTaken(String username){
        return userRepository.usernameAlreadyTaken(username) >= 1;
    }

    @Scheduled(cron = "*/10 * * * * *")
    public void helloWorld(){
        //when travel date - current date == 2 (2 days before the travel day), send a notification using firebase cloud messaging
        System.out.println("Hello world");
    }
}
