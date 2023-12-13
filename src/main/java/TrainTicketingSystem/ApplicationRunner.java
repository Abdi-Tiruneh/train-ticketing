package TrainTicketingSystem;

import TrainTicketingSystem.userManager.role.Role;
import TrainTicketingSystem.userManager.role.RoleRepository;
import TrainTicketingSystem.userManager.user.UserRepository;
import TrainTicketingSystem.userManager.user.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@ConditionalOnProperty(prefix = "database", name = "seed", havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class ApplicationRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    /**
     * Initializes the database with preloaded data upon application startup.
     */
    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            try {
                // Create and save roles
                List<Role> roles = createAttendanceSystemRoles();
                roles = roleRepository.saveAll(roles);

                // Create and save user
                Users johnDoe = createUser(roles.get(0));
                userRepository.save(johnDoe);

                log.info("ApplicationRunner => Preloaded organization, roles and admin user");
            } catch (Exception ex) {
                log.error("ApplicationRunner Preloading Error: {}", ex.getMessage());
                throw new RuntimeException("ApplicationRunner Preloading Error ", ex);
            }
        };
    }

    private List<Role> createAttendanceSystemRoles() {
        Role admin = new Role("ADMIN", "Full control and access to all features of the app, including managing users and system configurations.");
        Role user = new Role("USER", "Basic access to view train schedules, search for trains, and book tickets.");

        return List.of(admin, user);
    }

    private Users createUser(Role role) {
        return Users.builder()
                .username("john@admin.com")
                .fullName("John Doe")
                .password(passwordEncoder.encode("123456"))
                .fullName("Joe Doe")
                .role(role)
                .build();
    }
}