package hu.flowacademy.kappaspring.reallife.bootstrap;

import com.github.javafaker.Faker;
import hu.flowacademy.kappaspring.reallife.model.Blogpost;
import hu.flowacademy.kappaspring.reallife.model.User;
import hu.flowacademy.kappaspring.reallife.repository.BlogpostJpaRepository;
import hu.flowacademy.kappaspring.reallife.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class InitDataLoader implements CommandLineRunner {

    private final BlogpostJpaRepository blogpostJpaRepository;
    private final UserRepository userRepository;
    private final Faker faker;

    @Override
    public void run(String... args) throws Exception {
        List<User> users = userRepository.saveAll(populateUsers());

        List<Blogpost> blogposts = blogpostJpaRepository.saveAll(
                populateBlogposts(users)
        );

        log.info("Generated {} blogposts", blogposts.size());
    }

    private List<User> populateUsers() {
        return IntStream.range(0, 3)
                .mapToObj(value ->
                        User.builder()
                                .id(UUID.randomUUID().toString())
                                .username(faker.name().username())
                                .fullName(faker.starTrek().character())
                                .build())
                .collect(Collectors.toList());
    }

    private List<Blogpost> populateBlogposts(List<User> users) {
        return IntStream.range(0, 10)
                .mapToObj(value -> Blogpost.builder()
                        .id(UUID.randomUUID().toString())
                        .title(faker.book().title())
                        .description(faker.chuckNorris().fact())
                        .createdAt(LocalDateTime.now())
                        .publisher(users.get(new Random().nextInt(users.size())))
                        .build())
                .collect(Collectors.toList());
    }
}
