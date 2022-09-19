package hamdam.bookee.APIs.role_request.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

// TODO: 9/2/22 naming
@Getter
@Setter
public class RoleRequestDTO {
    // TODO: 9/2/22 why requesting some role works by role name, there is id in AppRole
    @JsonProperty("role_id")
    @NotNull(message = "role_id name can not be blank!")
    private Long roleId;

    @Size(max = 200, message = "Description size is too long!")
    private String description;
}
