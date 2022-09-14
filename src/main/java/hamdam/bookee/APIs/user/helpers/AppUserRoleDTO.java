package hamdam.bookee.APIs.user.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import hamdam.bookee.APIs.role.AppRoleEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@AllArgsConstructor
public class AppUserRoleDTO {

    @JsonProperty("role_id")
    private Long id;
    @JsonProperty("role_name")
    private String roleName;

    public AppUserRoleDTO(AppRoleEntity entity) {
        BeanUtils.copyProperties(entity, this);
    }
}
