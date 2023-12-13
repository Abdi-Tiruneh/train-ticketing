package TrainTicketingSystem.utils;

import TrainTicketingSystem.userManager.user.UserRepository;
import TrainTicketingSystem.userManager.user.Users;
import TrainTicketingSystem.exceptions.customExceptions.UnauthorizedException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentlyLoggedInUser {

    private final UserRepository userRepository;

    public CurrentlyLoggedInUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Users getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken)
            throw new UnauthorizedException("Access denied. Please provide a valid authentication token.");

        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("Please login again and try."));
        // If a user changes his or her email address, he or she must log in again.
    }
}