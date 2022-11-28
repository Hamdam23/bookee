package hamdam.bookee.APIs.user.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SetUserImageDTO {

    @JsonProperty("image_id")
    private Long id;
    @JsonProperty("image_location")
    private String location;
}
