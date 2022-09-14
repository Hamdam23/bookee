package hamdam.bookee.APIs.user.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import hamdam.bookee.APIs.image.ImagEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class AppUserImageDTO {

    @JsonProperty("image_id")
    private Long id;
    @JsonProperty("image_location")
    private String location;

    public AppUserImageDTO(ImagEntity entity) {
        BeanUtils.copyProperties(entity ,this);
    }
}
