package TrainTicketingSystem.ticket.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class TicketBookReq {

    @NotNull
    private Set<String> seatNumbers;
}
