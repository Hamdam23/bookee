package hamdam.bookee.APIs.role;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.Set;

@Getter
@Setter
public class AppRoleResponseDTO {

    @JsonProperty("role_id")
    private Long id;
    @JsonProperty("role_name")
    private String roleName;
    @JsonProperty("permissions")
    private Set<Permissions> permissions = Collections.emptySet();
}
