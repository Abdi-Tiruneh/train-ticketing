package TrainTicketingSystem.train;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {
    List<Train> findAllByTrainNameContainingIgnoreCase(String name, Sort sort);

    List<Train> findAllByArrivalStationContainingIgnoreCase(String arrivalStation, Sort sort);
    List<Train> findAllByScheduledTrue(Sort sort);
    List<Train> findAllByDepartureStationContainingIgnoreCase(String departureStation, Sort sort);

}
