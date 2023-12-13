package TrainTicketingSystem.utils;

import TrainTicketingSystem.userManager.user.Users;
import org.springframework.security.access.AccessDeniedException;

public class RoleChecker {

    public static void validateAdminUser(Users admin) {
        if (!admin.getRole().getRoleName().equals("ADMIN"))
            throw new AccessDeniedException("Only Admin users can perform this operation");
    }
}
