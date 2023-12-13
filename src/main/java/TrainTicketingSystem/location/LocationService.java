package TrainTicketingSystem.location;

import TrainTicketingSystem.utils.CurrentlyLoggedInUser;
import TrainTicketingSystem.utils.RoleChecker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final CurrentlyLoggedInUser inUser;

    public LocationService(LocationRepository locationRepository, CurrentlyLoggedInUser inUser) {
        this.locationRepository = locationRepository;
        this.inUser = inUser;
    }

    @Transactional
    public Location createLocation(Location newLocation) {
        RoleChecker.validateAdminUser(inUser.getUser());

        Location location = new Location();
        location.setName(newLocation.getName().trim());
        location.setDescription(newLocation.getDescription().trim());

        return locationRepository.save(location);
    }

    @Transactional
    public Location updateLocation(Long id, Location updatedLocation) {
        RoleChecker.validateAdminUser(inUser.getUser());

        Location existingLocation = getLocationById(id);

        if (updatedLocation.getName() != null)
            existingLocation.setName(updatedLocation.getName().trim());

        if (updatedLocation.getDescription() != null)
            existingLocation.setDescription(updatedLocation.getDescription().trim());

        return locationRepository.save(existingLocation);
    }

    @Transactional(readOnly = true)
    public Location getLocationById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + id));
    }


    @Transactional(readOnly = true)
    public List<Location> getLocations(String name) {
        Sort sort = Sort.by(Sort.Order.asc("name"));

        if (name != null)
            return locationRepository.findAllByNameContainingIgnoreCase(name, sort);

        return locationRepository.findAll(sort);
    }


    @Transactional
    public void deleteLocation(Long id) {
        RoleChecker.validateAdminUser(inUser.getUser());

        Location existingLocation = getLocationById(id);
        existingLocation.setDeleted(true);

        locationRepository.save(existingLocation);
    }

}
