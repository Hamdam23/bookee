package hamdam.bookee.APIs.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserImageDTO {
    @JsonProperty("image_id")
    private Long imageId;
}
