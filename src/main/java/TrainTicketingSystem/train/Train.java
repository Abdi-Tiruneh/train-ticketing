package TrainTicketingSystem.train;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "TTS_trains")
@SQLDelete(sql = "UPDATE TTS_trains SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Train {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String trainName;

    @Column(nullable = false)
    private String trainNumber;

    @Column(nullable = false)
    private int totalCoach;

    @Column(nullable = false)
    private int seatingCapacity;

    @Column(nullable = false)
    private String departureStation;

    @Column(nullable = false)
    private String arrivalStation;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted")
    @JsonIgnore
    private boolean deleted;

}