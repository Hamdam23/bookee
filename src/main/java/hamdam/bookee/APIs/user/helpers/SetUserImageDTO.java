package hamdam.bookee.APIs.user.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * It's a DTO that represents the data that is sent to the server
 * when a user wants to set their profile image
 */
@Getter
@Setter
@NoArgsConstructor
public class SetUserImageDTO {

    @JsonProperty("image_id")
    private Long id;
    @JsonProperty("image_location")
    private String location;
}
