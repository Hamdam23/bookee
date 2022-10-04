package hamdam.bookee.APIs.user.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class AppUserRequestDTO {

    @NotBlank(message = "name can not be empty!")
    private String name;

    @NotBlank(message = "name can not be empty!")
    private String userName;

    @JsonProperty("role_id")
    @NotNull(message = "role_id can not be null!")
    private Long roleId;

    @JsonProperty("image")
    @NotNull(message = "image_id can not be null!")
    private Long imageId;

    public AppUserRequestDTO(String name, String userName, Long roleId, Long imageId) {
        this.name = name;
        this.userName = userName;
        this.roleId = roleId;
        this.imageId = imageId;
    }
}
