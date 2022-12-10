package hamdam.bookee.APIs.role.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import hamdam.bookee.APIs.role.Permissions;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.Set;

/**
 * It's a DTO that represents the response of the API endpoint that returns a single role
 */
@Getter
@Setter
@NoArgsConstructor
public class AppRoleResponseDTO {

    @JsonProperty("role_id")
    private Long id;

    @JsonProperty("role_name")
    private String roleName;

    @JsonProperty("is_default")
    private boolean isDefault;

    @JsonProperty("permissions")
    private Set<Permissions> permissions = Collections.emptySet();
}
