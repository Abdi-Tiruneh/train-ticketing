package TrainTicketingSystem.userManager.user;

import TrainTicketingSystem.exceptions.customExceptions.ResourceAlreadyExistsException;
import TrainTicketingSystem.userManager.role.Role;
import TrainTicketingSystem.userManager.role.RoleService;
import TrainTicketingSystem.userManager.user.dto.UserRegistrationReq;
import TrainTicketingSystem.userManager.user.dto.UserResponse;
import TrainTicketingSystem.userManager.user.dto.UserUpdateReq;
import TrainTicketingSystem.utils.CurrentlyLoggedInUser;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final CurrentlyLoggedInUser inUser;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse register(UserRegistrationReq userReq) {
        if (userRepository.findByUsername(userReq.getUsername()).isPresent())
            throw new ResourceAlreadyExistsException("Username is already taken");

        Role role = roleService.getRoleByRoleName("USER");

        Users user = Users.builder()
                .username(userReq.getUsername())
                .fullName(userReq.getFullName())
                .password(passwordEncoder.encode(userReq.getPassword()))
                .role(role)
                .build();

        user = userRepository.save(user);
        return UserResponse.toResponse(user);
    }

    //users edit their account
    @Override
    public UserResponse editUser(UserUpdateReq updateReq) {
        Users user = inUser.getUser();
        return editUser(updateReq, user);
    }


    //admin for other users
    @Override
    public UserResponse editUser(Long userId, UserUpdateReq updateReq) {
        validateAdminUser(inUser.getUser());
        Users user = getUserById(userId);
        return editUser(updateReq, user);
    }

    @Transactional
    private UserResponse editUser(UserUpdateReq updateReq, Users user) {
        if (updateReq.getFullName() != null)
            user.setFullName(updateReq.getFullName());

        // Update it if provided username is different from the current username
        if (updateReq.getUsername() != null && !user.getUsername().equals(updateReq.getUsername())) {
            // Check if the new username is already taken
            if (userRepository.findByUsername(updateReq.getUsername()).isPresent())
                throw new ResourceAlreadyExistsException("Username is already taken");

            user.setUsername(updateReq.getUsername());
        }

        user = userRepository.save(user);
        return UserResponse.toResponse(user);
    }


    @Override
    public Users getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));
    }

    @Override
    public Users getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));
    }

    @Override
    public void deleteUser(Long id) {
        getUserById(id);
        userRepository.deleteById(id);
    }

    @Override
    public UserResponse me() {
        Users user = inUser.getUser();
        return UserResponse.toResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers(String role) {
        List<Users> users;

        if (!isValidRole(role))
            users = userRepository.findAll(Sort.by(Sort.Order.asc("id")));
        else
            users = userRepository.findByRoleRoleName(role.toUpperCase(), Sort.by(Sort.Order.asc("id")));

        return users.stream()
                .map(UserResponse::toResponse)
                .toList();
    }

    private boolean isValidRole(String role) {
        return role != null && !role.isEmpty() &&
                (role.equalsIgnoreCase("ADMIN") ||
                        role.equalsIgnoreCase("MANAGER") ||
                        role.equalsIgnoreCase("USER"));
    }

    private void validateAdminUser(Users admin) {
        if (!admin.getRole().getRoleName().equals("ADMIN"))
            throw new AccessDeniedException("Only admin users can perform this operation");
    }

}
