package hamdam.bookee.APIs.image;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // TODO: 9/2/22 name & json
    private String imageName;

    private String location;

    public Image(String imageName, String location) {
        this.imageName = imageName;
        this.location = location;
    }
}
