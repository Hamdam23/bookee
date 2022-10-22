package hamdam.bookee.APIs.image;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    public ImageDTO(ImageEntity entity) {
        BeanUtils.copyProperties(entity, this);
    }
}
