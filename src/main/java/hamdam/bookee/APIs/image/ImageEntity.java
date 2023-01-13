package hamdam.bookee.APIs.image;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import java.time.LocalDateTime;

import static hamdam.bookee.tools.constants.Patterns.TIMESTAMP_PATTERN;
import static hamdam.bookee.tools.constants.TableNames.TABLE_NAME_IMAGE;

/**
 * It's a JPA entity that represents an Image
 */
@Entity
@Table(name = TABLE_NAME_IMAGE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("image_name")
    private String imageName;

    @Column(nullable = false)
    private String url;

    @JsonFormat(pattern = TIMESTAMP_PATTERN)
    @UpdateTimestamp
    private LocalDateTime timeStamp;
}
