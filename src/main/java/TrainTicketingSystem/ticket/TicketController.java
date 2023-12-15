package TrainTicketingSystem.ticket;

import TrainTicketingSystem.ticket.dto.TicketBookReq;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
@Tag(name = "Ticket API.")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getTickets(@RequestParam Long trainId, @RequestParam(required = false) String status) {
        return ResponseEntity.ok(ticketService.getTickets(trainId, status));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<Ticket>> getMyTickets(@RequestParam Long trainId) {
        return ResponseEntity.ok(ticketService.getMyTickets(trainId));
    }

    @PutMapping("/book")
    public ResponseEntity<List<Ticket>> bookTickets(@RequestParam Long trainId, @RequestBody @Valid TicketBookReq ticketBookReq) {
        return ResponseEntity.ok(ticketService.bookTickets(trainId, ticketBookReq));
    }

    @PutMapping("/cancel")
    public ResponseEntity<List<Ticket>> cancelTickets(@RequestParam Long trainId, @RequestBody @Valid TicketBookReq ticketBookReq) {
        return ResponseEntity.ok(ticketService.cancelTickets(trainId, ticketBookReq));
    }

    @PutMapping("/confirm")
    public ResponseEntity<List<Ticket>> confirmTickets(@RequestParam Long trainId, @RequestBody @Valid TicketBookReq ticketBookReq) {
        return ResponseEntity.ok(ticketService.confirmTickets(trainId, ticketBookReq));
    }
}
