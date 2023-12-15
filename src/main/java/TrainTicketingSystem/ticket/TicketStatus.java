package TrainTicketingSystem.ticket;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum TicketStatus {
    AVAILABLE,
    BOOKED,
    CONFIRMED;

    public static TicketStatus fromString(String status) {
        for (TicketStatus ticketStatus : TicketStatus.values()) {
            if (ticketStatus.name().equalsIgnoreCase(status)) return ticketStatus;
        }
        throw new IllegalArgumentException("Invalid TicketStatus: " + status + ". Allowed values are: " + getAllowedValues());
    }

    private static String getAllowedValues() {
        return Arrays.stream(TicketStatus.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}
