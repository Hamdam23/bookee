package hamdam.bookee.APIs.image.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import hamdam.bookee.APIs.image.ImageEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@NoArgsConstructor
public class ImageDTO {
    private Long id;

    @JsonProperty("image_name")
    private String imageName;

    @JsonProperty("location")
    private String location;

    public ImageDTO(ImageEntity entity) {
        BeanUtils.copyProperties(entity, this);
    }
}
