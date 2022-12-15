package hamdam.bookee.APIs.role_request.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import hamdam.bookee.APIs.image.ImageEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * It's a DTO that represents a request to add a user to a role
 */
@Getter
@Setter
@AllArgsConstructor
public class UserOnRoleRequestDTO {

    private String name;
    @JsonProperty("username")
    private String username;
    @JsonProperty("user_role")
    private String userRole;
    @JsonProperty("user_image")
    private ImageEntity userImageEntity;
}
