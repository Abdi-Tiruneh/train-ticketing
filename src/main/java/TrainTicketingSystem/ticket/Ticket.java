package TrainTicketingSystem.ticket;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "TTS_tickets")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(nullable = false, name = "train_id")
    private Long trainId;

    private boolean booked;

    private LocalDateTime bookingTime;

    @Column(nullable = false, name = "fair_per_seat")
    private double fairPerSeat;

    @Column(nullable = false, name = "seat_number")
    private String seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status;
}
