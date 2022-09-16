package hamdam.bookee.APIs.role_request.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

// TODO: 9/2/22 naming
@Getter
@Setter
public class RoleRequestDTO {
    // TODO: 9/2/22 why requesting some role works by role name, there is id in AppRole
    @JsonProperty("role_id")
    private Long roleId;
}
