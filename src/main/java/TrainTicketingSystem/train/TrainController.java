package TrainTicketingSystem.train;

import TrainTicketingSystem.train.dto.TrainScheduleReq;
import TrainTicketingSystem.train.dto.TrainReq;
import TrainTicketingSystem.utils.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trains")
@Tag(name = "Train API.")
public class TrainController {

    private final TrainService trainService;

    public TrainController(TrainService trainService) {
        this.trainService = trainService;
    }

    @GetMapping
    public ResponseEntity<List<Train>> getTrains(@RequestParam(required = false) String trainName) {
        return ResponseEntity.ok(trainService.getTrains(trainName));
    }

    @GetMapping("/station")
    public ResponseEntity<List<Train>> getTrainsByStation(@RequestParam String stationName, @RequestParam Integer station) {
        return ResponseEntity.ok(trainService.getTrainsByStation(stationName,station));
    }

    @PostMapping
    public ResponseEntity<Train> createTrain(@RequestBody @Valid TrainReq trainReq) {
        Train response = trainService.createTrain(trainReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Train> updateTrain(@PathVariable Long id, @RequestBody TrainReq trainReq) {
        return ResponseEntity.ok(trainService.updateTrain(id, trainReq));
    }

    @PutMapping("/schedule/{id}")
    public ResponseEntity<Train> scheduleTrain(@PathVariable Long id, @RequestBody @Valid TrainScheduleReq trainScheduleReq) {
        return ResponseEntity.ok(trainService.scheduleTrain(id, trainScheduleReq));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteTrain(@PathVariable Long id) {
        trainService.deleteTrain(id);
        return ApiResponse.success("Deleted Successfully");
    }

}


