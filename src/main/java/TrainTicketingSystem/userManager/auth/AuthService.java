package TrainTicketingSystem.userManager.auth;

import TrainTicketingSystem.userManager.auth.dto.ChangePassword;
import TrainTicketingSystem.userManager.auth.dto.ResetPassword;
import TrainTicketingSystem.utils.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    ResponseEntity<ApiResponse> changePassword(ChangePassword changePassword);

    ResponseEntity<ApiResponse> resetPassword(String phoneNumber, ResetPassword resetPassword);

}
