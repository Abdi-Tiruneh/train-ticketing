package TrainTicketingSystem.location;

import TrainTicketingSystem.utils.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
@Tag(name = "Location API.")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }


    @GetMapping
    public ResponseEntity<List<Location>> getLocations(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(locationService.getLocations(name));
    }

    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestBody @Valid Location location) {
        Location response = locationService.createLocation(location);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable Long id, @RequestBody Location location) {
        return ResponseEntity.ok(locationService.updateLocation(id, location));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ApiResponse.success("Deleted Successfully");
    }

}


