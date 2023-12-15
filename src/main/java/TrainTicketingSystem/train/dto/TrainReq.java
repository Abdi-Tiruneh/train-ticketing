package TrainTicketingSystem.train.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TrainReq {
    @NotBlank
    private String trainName;

    @NotBlank
    private String trainNumber;

    @NotNull
    private Integer totalCoach;

    @NotNull
    private Integer seatingCapacityPerCouch;

    @NotNull
    private Double fairPerSeat;

    @NotBlank
    private String departureStation;

    @NotBlank
    private String arrivalStation;
}
