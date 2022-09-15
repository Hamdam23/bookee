package hamdam.bookee.APIs.role.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role.Permissions;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Collections;
import java.util.Set;

@Getter
@Setter
public class AppRoleResponseDTO {

    @JsonProperty("role_id")
    private Long id;

    @JsonProperty("role_name")
    private String roleName;

    @JsonProperty("is_default")
    private boolean isDefault;

    @JsonProperty("permissions")
    private Set<Permissions> permissions = Collections.emptySet();

    public AppRoleResponseDTO(AppRoleEntity entity) {
        BeanUtils.copyProperties(entity, this);
    }
}
