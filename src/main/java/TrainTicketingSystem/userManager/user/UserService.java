package TrainTicketingSystem.userManager.user;

import TrainTicketingSystem.userManager.user.dto.UserRegistrationReq;
import TrainTicketingSystem.userManager.user.dto.UserResponse;
import TrainTicketingSystem.userManager.user.dto.UserUpdateReq;

import java.util.List;

public interface UserService {
    UserResponse register(UserRegistrationReq userReq, String roleName);

    UserResponse me();

    List<UserResponse> getAllUsers(String role);

    UserResponse editUser(UserUpdateReq updateReq);

    UserResponse editUser(Long userId, UserUpdateReq updateReq);

    Users getUserByUsername(String email);

    Users getUserById(Long userId);

    void deleteUser(Long id);
}
