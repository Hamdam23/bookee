package hamdam.bookee.APIs.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "images")
@Getter
@Setter
@NoArgsConstructor
public class ImagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // TODO: 9/2/22 name & json
    @JsonProperty("image_name")
    private String imageName;

    private String location;

    public ImagEntity(String imageName, String location) {
        this.imageName = imageName;
        this.location = location;
    }
}
