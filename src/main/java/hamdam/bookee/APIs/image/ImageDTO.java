package hamdam.bookee.APIs.image;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
public class ImageDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long imageId;
}
