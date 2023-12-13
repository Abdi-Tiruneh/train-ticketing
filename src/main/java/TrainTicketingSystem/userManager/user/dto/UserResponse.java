package TrainTicketingSystem.userManager.user.dto;

import TrainTicketingSystem.userManager.user.Users;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    private Long userId;
    private String fullName;
    private String username;
    private String role;
    private String lastLoggedIn;
    private String createdAt;
    private String updatedAt;

    public static UserResponse toResponse(Users user) {
        return UserResponse.builder()
                .userId(user.getId())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .role(user.getRole().getRoleName())
                .lastLoggedIn(user.getLastLoggedIn() != null ? user.getLastLoggedIn().toString() : null)
                .createdAt(user.getCreatedAt().toString())
                .updatedAt(user.getUpdatedAt().toString())
                .build();
    }
}
