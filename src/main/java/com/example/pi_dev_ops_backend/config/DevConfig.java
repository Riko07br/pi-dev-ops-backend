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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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
        List<UserProfile> userProfileList = new ArrayList<>();
        for (int i = 0; i < 10; i++)
        {
            User u = userRepository.save(new User("user" + i + "@mail.com", password));
            UserProfile p = new UserProfile(
                    "User " + i,
                    "123456789-" + i,
                    "Street " + i,
                    "12345",
                    "Document " + i,
                    "Description " + i,
                    "Title " + i);
            p.setUser(u);
            p.addSkill(new Skill(i % 2 == 0 ? "Java" : "Python"));
            p.addSkill(new Skill(i % 2 == 0 ? "Spring boot" : "Django"));
            userProfileList.add(userProfileRepository.save(p));
        }
        List<Listing> listingList = new ArrayList<>();
        for (int i = 0; i < 5; i++)
        {
            Listing l = new Listing("Listing " + i, 750f * i, "Description " + i, userProfileList.get(i));
            listingList.add(listingRepository.save(l));
        }

        for (int i = 6; i < 10; i++)
        {
            boolean finished = i % 2 == 0;
            ContractedListing cl = new ContractedListing(
                    finished ? "FINISHED" : "CONTRACTED",
                    "Request " + i,
                    Instant.now().minus(10 * (i - 5), ChronoUnit.DAYS),
                    Instant.now().plus(10 * (i - 5), ChronoUnit.DAYS),
                    listingList.get(i - 6),
                    userProfileList.get(i));
            contractedListingRepository.save(cl);
        }

        User u4 = userRepository.save(new User("fred@mail.com", password));
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
        p2.addSkill(new Skill("Sing"));
        p2 = userProfileRepository.save(p2);

        Listing l1 = new Listing("Listing 1", 1000f, "Description 1", p2);
        l1 = listingRepository.save(l1);
        Listing l2 = new Listing("Listing 2", 2000f, "Description 2", p2);
        l2 = listingRepository.save(l2);

        ContractedListing cl1 = new ContractedListing("Pending", "Request 1", null, null, l1, userProfileList.get(0));
        contractedListingRepository.save(cl1);
        ContractedListing cl2 = new ContractedListing("Accepted", "Request 2", Instant.now(), null, l2, userProfileList.get(1));
        contractedListingRepository.save(cl2);
        ContractedListing cl3 = new ContractedListing("Finished", "Request 3", Instant.now(), Instant.now(), l1, userProfileList.get(2));
        contractedListingRepository.save(cl3);

    }
}
