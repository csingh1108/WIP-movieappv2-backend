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
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(columnDefinition = "TEXT")
    private String synopsis;
    private Integer releaseYear;
    private Integer duration;
    private String imgUrl;
    private String videoUrl;
    private String rating;
    private String genre;
    private Boolean isFeatured;
    @OneToMany(mappedBy = "movie")
    private List<MovieTime> movieTimes;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
