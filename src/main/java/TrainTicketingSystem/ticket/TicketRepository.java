package TrainTicketingSystem.ticket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByTrainId(Long trainId);

    List<Ticket> findByTrainIdAndStatusInAndUserId(Long trainId, List<TicketStatus> statusList, Long userId);

    List<Ticket> findByTrainIdAndStatusAndSeatNumberIn(Long trainId,TicketStatus ticketStatus, Set<String> seatNumbers);

    List<Ticket> findByTrainIdAndStatus(Long trainId, TicketStatus ticketStatus);

    List<Ticket> findByTrainIdAndStatusAndSeatNumberInAndUserId(Long trainId, TicketStatus ticketStatus, Set<String> seatNumbers, Long userId);
}
