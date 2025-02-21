package com.example.pi_dev_ops_backend.config;

import com.example.pi_dev_ops_backend.domain.entities.User;
import com.example.pi_dev_ops_backend.domain.entities.UserProfile;
import com.example.pi_dev_ops_backend.repository.UserProfileRepository;
import com.example.pi_dev_ops_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile ({ "dev" })
public class DevConfig implements CommandLineRunner
{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    public void run(String... args) throws Exception
    {
        String password = "123456";

        User u1 = userRepository.save(new User(null, "user1@mail.com", password));
        User u2 = userRepository.save(new User(null, "user2@mail.com", password));

        User u3 = userRepository.save(new User(null, "user3@mail.com", password));
        UserProfile p1 = new UserProfile(null, "User 3", "123456789", "Street 1", "12345");
        p1.setUser(u3);
        userProfileRepository.save(p1);

        User u4 = userRepository.save(new User(null, "user4@mail.com", password));
        UserProfile p2 = new UserProfile(null, "User 4", "123456789", "Street 2", "12345");
        p2.setUser(u4);
        userProfileRepository.save(p2);
    }
}
