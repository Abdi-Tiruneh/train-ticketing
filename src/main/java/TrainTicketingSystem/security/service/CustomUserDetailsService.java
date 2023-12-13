package TrainTicketingSystem.security.service;

import TrainTicketingSystem.userManager.user.UserRepository;
import TrainTicketingSystem.userManager.user.Users;
import TrainTicketingSystem.userManager.user.dto.UserResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Users loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("There is no user with this username")
        );

        Collection<SimpleGrantedAuthority> authorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .toList();

        return user;
    }

    public void updateLastLogin(String username) {
        Optional<Users> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            Users adminUser = user.get();
            adminUser.setLastLoggedIn(LocalDateTime.now());
            userRepository.save(adminUser);
        }
    }

    public UserResponse getByUsername(String username) {
        Users user =  userRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );


        return UserResponse.toResponse(user);
    }
}