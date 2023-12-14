package TrainTicketingSystem.train.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class TrainScheduleReq {
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @NotBlank
    private String departureTime;
}



