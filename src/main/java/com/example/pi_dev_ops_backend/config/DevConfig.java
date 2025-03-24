package com.example.pi_dev_ops_backend.config;

import com.example.pi_dev_ops_backend.domain.entities.ContractedListing;
import com.example.pi_dev_ops_backend.domain.entities.Listing;
import com.example.pi_dev_ops_backend.domain.entities.Skill;
import com.example.pi_dev_ops_backend.domain.entities.User;
import com.example.pi_dev_ops_backend.domain.entities.UserProfile;
import com.example.pi_dev_ops_backend.repository.ContractedListingRepository;
import com.example.pi_dev_ops_backend.repository.ListingRepository;
import com.example.pi_dev_ops_backend.repository.UserProfileRepository;
import com.example.pi_dev_ops_backend.repository.UserRepository;
import com.example.pi_dev_ops_backend.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Instant;

@Configuration
@Profile ({ "dev" })
public class DevConfig implements CommandLineRunner
{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private ListingRepository listingRepository;
    @Autowired
    private ContractedListingRepository contractedListingRepository;
    @Autowired
    private SecurityConfig securityConfiguration;

    @Override
    public void run(String... args) throws Exception
    {
        String password = securityConfiguration.passwordEncoder().encode("123456");

        User u1 = userRepository.save(new User("user1@mail.com", password));
        User u2 = userRepository.save(new User("user2@mail.com", password));

        User u3 = userRepository.save(new User("user3@mail.com", password));
        UserProfile p1 = new UserProfile(
                "User 3",
                "123456789",
                "Street 1",
                "12345",
                "Document 1",
                "Description 1",
                "Title 1");
        p1.setUser(u3);
        p1 = userProfileRepository.save(p1);

        User u4 = userRepository.save(new User("user4@mail.com", password));
        UserProfile p2 = new UserProfile(
                "Freddie Mercury",
                "987654321",
                "Street 2",
                "54321",
                "Document 2",
                "I make a super sonic man out of you",
                "Master of the universe");
        p2.setUser(u4);
        p2.addSkill(new Skill("Java"));
        p2.addSkill(new Skill("Spring Boot"));
        p2.addSkill(new Skill("Laravel"));
        p2 = userProfileRepository.save(p2);

        Listing l1 = new Listing("Listing 1", 1000f, "Description 1", p2);
        l1 = listingRepository.save(l1);
        Listing l2 = new Listing("Listing 2", 2000f, "Description 2", p2);
        l2 = listingRepository.save(l2);

        ContractedListing cl1 = new ContractedListing("Pending", "Request 1", null, null, l1, p1);
        contractedListingRepository.save(cl1);
        ContractedListing cl2 = new ContractedListing("Accepted", "Request 2", Instant.now(), null, l2, p1);
        contractedListingRepository.save(cl2);
        ContractedListing cl3 = new ContractedListing("Finished", "Request 3", Instant.now(), Instant.now(), l1, p1);
        contractedListingRepository.save(cl3);

    }
}
