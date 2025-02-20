package com.example.pi_dev_ops_backend.config;

import com.example.pi_dev_ops_backend.domain.entities.User;
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

    @Override
    public void run(String... args) throws Exception
    {
        String password = "123456";

        User u1 = userRepository.save(new User(null, "user1@mail.com", password));
        User u2 = userRepository.save(new User(null, "user2@mail.com", password));
        User u3 = userRepository.save(new User(null, "user3@mail.com", password));

    }
}
