package hamdam.bookee.APIs.user.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import hamdam.bookee.APIs.image.ImageEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class SetUserImageDTO {

    @JsonProperty("image_id")
    private Long id;
    @JsonProperty("image_location")
    private String location;

    public SetUserImageDTO(ImageEntity entity) {
        BeanUtils.copyProperties(entity, this);
    }
}
