package hamdam.bookee.APIs.user.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import hamdam.bookee.APIs.image.ImagEntity;
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

    public SetUserImageDTO(ImagEntity entity) {
        BeanUtils.copyProperties(entity ,this);
    }
}
