package hamdam.bookee.APIs.image.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * It's a DTO that contains the ID of an image
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserImageDTO {
    @JsonProperty("image_id")
    private Long imageId;
}
