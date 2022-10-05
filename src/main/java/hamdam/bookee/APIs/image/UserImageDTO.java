package hamdam.bookee.APIs.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

// TODO: 9/2/22 name
@Data
public class UserImageDTO {
    @JsonProperty("image_id")
    private Long imageId;
}
