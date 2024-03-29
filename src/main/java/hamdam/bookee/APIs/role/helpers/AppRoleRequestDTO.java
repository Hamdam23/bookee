package hamdam.bookee.APIs.role.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import hamdam.bookee.APIs.role.Permissions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * It's a DTO class that contains the role name, whether it's a default role, and a set of permissions
 */
@Getter
@Setter
@AllArgsConstructor
public class AppRoleRequestDTO {

    @JsonProperty("role_name")
    @NotBlank(message = "role_name name can not be blank!")
    private String roleName;

    @JsonProperty("is_default")
    private boolean isDefault;

    @JsonProperty("permissions")
    private Set<Permissions> permissions;
}
