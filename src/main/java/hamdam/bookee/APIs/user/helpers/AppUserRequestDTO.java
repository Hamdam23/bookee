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

    @NotBlank()
    private String name;

    @NotBlank()
    private String username;

    @JsonProperty("role_id")
    @NotNull()
    private Long roleId;

    @JsonProperty("image")
    @NotNull()
    private Long imageId;
}
