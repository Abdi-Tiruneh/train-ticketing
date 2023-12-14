package TrainTicketingSystem.train;

import TrainTicketingSystem.train.dto.TrainScheduleReq;
import TrainTicketingSystem.train.dto.TrainReq;
import TrainTicketingSystem.utils.CurrentlyLoggedInUser;
import TrainTicketingSystem.utils.RoleChecker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TrainService {

    private final TrainRepository trainRepository;
    private final CurrentlyLoggedInUser inUser;

    public TrainService(TrainRepository trainRepository, CurrentlyLoggedInUser inUser) {
        this.trainRepository = trainRepository;
        this.inUser = inUser;
    }


    public Train createTrain(TrainReq trainReq) {
        RoleChecker.validateAdminUser(inUser.getUser());
        validateDepartureArrival(trainReq.getDepartureStation(), trainReq.getArrivalStation());

        Train train = new Train();
        train.setTrainName(trainReq.getTrainName());
        train.setTrainNumber(trainReq.getTrainNumber());
        train.setTotalCoach(trainReq.getTotalCoach());
        train.setSeatingCapacity(trainReq.getSeatingCapacity());
        train.setFairPerSeat(trainReq.getFairPerSeat());
        train.setDepartureStation(trainReq.getDepartureStation());
        train.setArrivalStation(trainReq.getArrivalStation());

        return trainRepository.save(train);
    }

    @Transactional
    public Train updateTrain(Long id, TrainReq trainReq) {
        RoleChecker.validateAdminUser(inUser.getUser());

        Train train = getTrainById(id);

        if (trainReq.getTrainName() != null)
            train.setTrainName(trainReq.getTrainName().trim());

        if (trainReq.getTrainNumber() != null)
            train.setTrainNumber(trainReq.getTrainNumber().trim());

        if (trainReq.getFairPerSeat() != null)
            train.setFairPerSeat(trainReq.getFairPerSeat());

        if (trainReq.getTotalCoach() != null)
            train.setTotalCoach(trainReq.getTotalCoach());

        if (trainReq.getSeatingCapacity() != null)
            train.setSeatingCapacity(trainReq.getSeatingCapacity());

        if (trainReq.getDepartureStation() != null)
            train.setDepartureStation(trainReq.getDepartureStation().trim());

        if (trainReq.getArrivalStation() != null)
            train.setArrivalStation(trainReq.getArrivalStation().trim());

        return trainRepository.save(train);
    }


    @Transactional
    public Train scheduleTrain(Long id, TrainScheduleReq trainScheduleReq) {
        RoleChecker.validateAdminUser(inUser.getUser());

        Train train = getTrainById(id);

        if (train.getDepartureTime() != null) {
            String errorMessage = String.format(
                    "Train is already scheduled for departure at %s from %s to %s",
                    train.getDepartureTime(),
                    train.getDepartureStation(),
                    train.getArrivalStation()
            );
            throw new IllegalArgumentException(errorMessage);
        }

        String dateTimeString = trainScheduleReq.getDepartureTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);

        train.setDepartureTime(dateTime.withSecond(0));
        return trainRepository.save(train);
    }


    @Transactional(readOnly = true)
    public Train getTrainById(Long id) {
        return trainRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Train not found with id: " + id));
    }


    @Transactional(readOnly = true)
    public List<Train> getTrains(String name) {
        Sort sort = Sort.by(Sort.Order.asc("trainName"));

        if (name != null)
            return trainRepository.findAllByTrainNameContainingIgnoreCase(name, sort);

        return trainRepository.findAll(sort);
    }


    //station = 0 => departure
    //station = 1 => arrival
    @Transactional(readOnly = true)
    public List<Train> getTrainsByStation(String stationName, int station) {
        Sort sort = Sort.by(Sort.Order.asc("trainName"));
        if (station == 1)
            return trainRepository.findAllByArrivalStationContainingIgnoreCase(stationName, sort);
        else
            return trainRepository.findAllByDepartureStationContainingIgnoreCase(stationName, sort);
    }

    @Transactional
    public void deleteTrain(Long id) {
        RoleChecker.validateAdminUser(inUser.getUser());

        Train train = getTrainById(id);
        train.setDeleted(true);

        trainRepository.save(train);
    }

    public void validateDepartureArrival(String departureStation, String arrivalStation) {
        if (departureStation.equalsIgnoreCase(arrivalStation))
            throw new IllegalArgumentException("Departure and arrival destinations must be different.");
    }

}
