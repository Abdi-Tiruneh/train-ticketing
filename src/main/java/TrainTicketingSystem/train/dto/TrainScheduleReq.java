package TrainTicketingSystem.train.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TrainScheduleReq {
    @NotBlank
    private String departureTime;
}



