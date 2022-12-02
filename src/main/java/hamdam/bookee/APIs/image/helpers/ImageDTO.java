package hamdam.bookee.APIs.image.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImageDTO {
    private Long id;

    @JsonProperty("image_name")
    private String imageName;

    @JsonProperty("url")
    private String url;

}
