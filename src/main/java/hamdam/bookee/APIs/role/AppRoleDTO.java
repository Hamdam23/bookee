package hamdam.bookee.APIs.role;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppRoleDTO {
    // TODO: 9/2/22 name & json
    private String roleName;
    @JsonProperty("is_default")
    private boolean isDefault;
    private Set<Permissions> permissions;
}