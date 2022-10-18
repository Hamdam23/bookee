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
    // TODO: 9/2/22 name & json
    @JsonProperty("image_name")
    private String imageName;

    public ImageDTO(ImageEntity entity){
        BeanUtils.copyProperties(entity, this);
    }
}
