package hamdam.bookee.APIs.role.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import hamdam.bookee.APIs.role.Permissions;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class AppRoleRequestDTO {

    // TODO: 9/2/22 name & json
    @JsonProperty("role_name")
    private String roleName;

    @JsonProperty("is_default")
    private boolean isDefault;

    @JsonProperty("permissions")
    private Set<Permissions> permissions;
}
