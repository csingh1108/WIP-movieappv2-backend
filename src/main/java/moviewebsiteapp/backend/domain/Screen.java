package moviewebsiteapp.backend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String screenName;
    private Integer capacity;

    @OneToMany(mappedBy = "screen")
    private List<MovieTime> movieTimes;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}
