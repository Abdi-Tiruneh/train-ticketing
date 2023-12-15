package TrainTicketingSystem.ticket;

import TrainTicketingSystem.ticket.dto.TicketBookReq;
import TrainTicketingSystem.train.Train;
import TrainTicketingSystem.utils.CurrentlyLoggedInUser;
import TrainTicketingSystem.utils.RoleChecker;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final CurrentlyLoggedInUser inUser;

    public TicketService(TicketRepository ticketRepository, CurrentlyLoggedInUser inUser) {
        this.ticketRepository = ticketRepository;
        this.inUser = inUser;
    }

    @Async
    @Transactional
    public void generateTickets(Train train) {
        int totalCoaches = train.getTotalCoach();
        int seatingCapacityPerCouch = train.getSeatingCapacityPerCouch();

        for (int coachIndex = 0; coachIndex < totalCoaches; coachIndex++) {
            String section = Character.toString((char) ('A' + coachIndex));
            List<Ticket> ticketList = new ArrayList<>();

            for (int seatIndex = 1; seatIndex <= seatingCapacityPerCouch; seatIndex++) {
                Ticket ticket = new Ticket();
                ticket.setTrainId(train.getId());
                ticket.setFairPerSeat(train.getFairPerSeat());
                ticket.setSeatNumber(section + seatIndex);
                ticket.setStatus(TicketStatus.AVAILABLE);
                ticketList.add(ticket);
            }

            //save for one couch
            ticketRepository.saveAll(ticketList);
        }
    }

    @Transactional(readOnly = true)
    public List<Ticket> getTickets(Long trainId, String status) {

        TicketStatus ticketStatus = (status != null) ? TicketStatus.fromString(status) : null;

        if (ticketStatus == TicketStatus.AVAILABLE)
            return ticketRepository.findByTrainIdAndStatus(trainId, ticketStatus);

        RoleChecker.validateAdminUser(inUser.getUser());
        return (ticketStatus == null) ? ticketRepository.findByTrainId(trainId) :
                ticketRepository.findByTrainIdAndStatus(trainId, ticketStatus);

    }

    @Transactional(readOnly = true)
    public List<Ticket> getMyTickets(Long trainId) {
        Long userId = inUser.getUser().getId();

        List<TicketStatus> ticketStatusList = List.of(TicketStatus.BOOKED, TicketStatus.CONFIRMED);

        return ticketRepository.findByTrainIdAndStatusInAndUserId(trainId, ticketStatusList, userId);
    }

    @Transactional
    public List<Ticket> bookTickets(Long trainId, TicketBookReq ticketBookReq) {
        Long userId = inUser.getUser().getId();
        Set<String> seatNumbers = ticketBookReq.getSeatNumbers();

        // Check seat availability
        List<Ticket> availableTickets = ticketRepository.findByTrainIdAndStatusAndSeatNumberIn(trainId, TicketStatus.AVAILABLE, seatNumbers);
        if (availableTickets.isEmpty())
            throw new IllegalArgumentException("Please send valid seat Numbers.");

        // Book tickets
        List<Ticket> ticketList = availableTickets.stream()
                .map(ticket -> {
                    ticket.setUserId(userId);
                    ticket.setBooked(true);
                    ticket.setBookingTime(LocalDateTime.now());
                    ticket.setStatus(TicketStatus.BOOKED);
                    return ticket;
                })
                .toList();

        return ticketRepository.saveAll(ticketList);
    }

    @Transactional
    public List<Ticket> cancelTickets(Long trainId, TicketBookReq ticketBookReq) {
        Long userId = inUser.getUser().getId();
        Set<String> seatNumbers = ticketBookReq.getSeatNumbers();

        //get booked tickets
        List<Ticket> bookedTickets = ticketRepository.findByTrainIdAndStatusAndSeatNumberInAndUserId(trainId, TicketStatus.BOOKED, seatNumbers, userId);
        if (bookedTickets.isEmpty())
            throw new IllegalArgumentException("Please send valid booked seat Numbers.");

        //Cancel tickets
        List<Ticket> ticketList = bookedTickets.stream()
                .map(ticket -> {
                    ticket.setUserId(null);
                    ticket.setBooked(false);
                    ticket.setBookingTime(null);
                    ticket.setStatus(TicketStatus.AVAILABLE);
                    return ticket;
                })
                .toList();

        return ticketRepository.saveAll(ticketList);
    }

    @Transactional
    public List<Ticket> confirmTickets(Long trainId, TicketBookReq ticketBookReq) {
        RoleChecker.validateAdminUser(inUser.getUser());

        Set<String> seatNumbers = ticketBookReq.getSeatNumbers();

        //get booked tickets
        List<Ticket> bookedTickets = ticketRepository.findByTrainIdAndStatusAndSeatNumberIn(trainId, TicketStatus.BOOKED, seatNumbers);
        if (bookedTickets.isEmpty())
            throw new IllegalArgumentException("Please send valid booked seat Numbers.");

        //confirm tickets
        List<Ticket> ticketList = bookedTickets.stream()
                .map(ticket -> {
                    ticket.setStatus(TicketStatus.CONFIRMED);
                    return ticket;
                })
                .toList();

        return ticketRepository.saveAll(ticketList);
    }

}



