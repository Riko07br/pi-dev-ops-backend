package com.example.pi_dev_ops_backend.config;

import com.example.pi_dev_ops_backend.domain.entities.ContractedListing;
import com.example.pi_dev_ops_backend.domain.entities.Evaluation;
import com.example.pi_dev_ops_backend.domain.entities.Listing;
import com.example.pi_dev_ops_backend.domain.entities.Skill;
import com.example.pi_dev_ops_backend.domain.entities.User;
import com.example.pi_dev_ops_backend.domain.entities.UserProfile;
import com.example.pi_dev_ops_backend.repository.ContractedListingRepository;
import com.example.pi_dev_ops_backend.repository.EvaluationRepository;
import com.example.pi_dev_ops_backend.repository.ListingRepository;
import com.example.pi_dev_ops_backend.repository.SkillRepository;
import com.example.pi_dev_ops_backend.repository.UserProfileRepository;
import com.example.pi_dev_ops_backend.repository.UserRepository;
import com.example.pi_dev_ops_backend.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Profile ({ "dev", "seed" })
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
    private EvaluationRepository evaluationRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private SecurityConfig securityConfiguration;

    @Override
    public void run(String... args) throws Exception
    {
        String password = securityConfiguration.passwordEncoder().encode("123456");
        List<Skill> skillList = generateSkills();
        List<UserProfile> userProfileList = generateUserProfiles(30, skillList);
        List<Listing> listingList = new ArrayList<>();
        for (int i = 0; i < 20; i++)
        {
            Listing l = new Listing(
                    "Listing " + i,
                    750f * i,
                    "Description " + i,
                    "Location " + i,
                    LocalDate.of(2025, Math.round((i + 1) / 2f), i + 1),
                    userProfileList.get(Math.round(i / 2f)));
            l.addSkill(skillList.get(Math.round((i + 1)/4f)));
            listingList.add(listingRepository.save(l));
        }

        for (int i = 10; i < 20; i++)
        {
            boolean finished = i % 2 == 0;
            ContractedListing cl = new ContractedListing(
                    finished ? "ACCEPTED" : "CONTRACTED",
                    "Request " + i,
                    localDate(10 * (i - 5), true),
                    localDate(10 * (i - 5), false),
                    listingList.get(i - 6),
                    userProfileList.get(i));
            contractedListingRepository.save(cl);
            if (finished)
            {
                Evaluation e = new Evaluation("Good job", 4, getInstant(11 * (i - 5), true), cl);
                evaluationRepository.save(e);
            }
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
        p2.addSkill(skillList.get(0));
        p2.addSkill(skillList.get(1));
        p2.addSkill(skillList.get(2));
        p2.addSkill(skillRepository.save(new Skill("Sing")));
        p2 = userProfileRepository.save(p2);

        Listing l1 = new Listing("Listing 1", 1000f, "Description 1", "Location 1", LocalDate.of(2025, 2, 3), p2);
        l1.addSkill(skillList.get(2));
        l1.addSkill(skillList.get(3));
        l1.addSkill(skillList.get(4));
        l1 = listingRepository.save(l1);
        Listing l2 = new Listing("Listing 2", 2000f, "Description 2", "Location 2", LocalDate.of(2025, 8, 3), p2);
        l2.addSkill(skillList.get(3));
        l2.addSkill(skillList.get(4));
        l2 = listingRepository.save(l2);

        ContractedListing cl1 = new ContractedListing("CONTRACTED", "Request 1", null, null, l1, userProfileList.get(0));
        contractedListingRepository.save(cl1);
        ContractedListing cl2 = new ContractedListing("CANCELLED", "Request 2", localDate(1, true), null, l2, userProfileList.get(1));
        cl2 = contractedListingRepository.save(cl2);
        ContractedListing cl3 = new ContractedListing("ACCEPTED", "Request 3", localDate(2, true), LocalDate.now(), l1, userProfileList.get(2));
        cl3 = contractedListingRepository.save(cl3);

        Evaluation e1 = new Evaluation("Good job", 5, Instant.now(), cl2);
        evaluationRepository.save(e1);
        Evaluation e2 = new Evaluation("Bad job", 1, Instant.now(), cl3);
        evaluationRepository.save(e2);
    }

    private LocalDate localDate(long days, boolean isMinus)
    {
        LocalDate now = LocalDate.now();
        return !isMinus
                ? now.plusDays(days)
                : now.minusDays(days);
    }

    private Instant getInstant(long days, boolean isMinus)
    {
        Instant now = Instant.now();
        return !isMinus
                ? now.plus(days, ChronoUnit.DAYS)
                : now.minus(days, ChronoUnit.DAYS);
    }

    private List<UserProfile> generateUserProfiles(int amount, List<Skill> skillList)
    {
        List<UserProfile> userProfiles = new ArrayList<>();
        String password = securityConfiguration.passwordEncoder().encode("123456");
        for (int i = 0; i < amount; i++)
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
            p.addSkill(skillList.get(i % skillList.size()));
            userProfiles.add(p);
        }
        return userProfileRepository.saveAll(userProfiles);
    }

    private List<Skill> generateSkills()
    {
        List<Skill> skills = List.of(
                new Skill("Java"),
                new Skill("Spring Boot"),
                new Skill("Laravel"),
                new Skill("PHP"),
                new Skill("Python"),
                new Skill("Django"),
                new Skill("React"),
                new Skill("Angular"),
                new Skill("Vue"),
                new Skill("JavaScript")
        );
        return skillRepository.saveAll(skills);
    }
}
