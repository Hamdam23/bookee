package hamdam.bookee.APIs.user.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * It's a DTO that represents the request body of a POST request to the /users endpoint
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

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
