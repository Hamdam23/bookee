package hamdam.bookee.APIs.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // TODO: 9/2/22 name & json
    @JsonProperty("image_name")
    private String imageName;

    private String location;

    public Image(String imageName, String location) {
        this.imageName = imageName;
        this.location = location;
    }
}
