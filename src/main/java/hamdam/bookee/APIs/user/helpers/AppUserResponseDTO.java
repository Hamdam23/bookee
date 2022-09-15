package hamdam.bookee.APIs.user.helpers;

import hamdam.bookee.APIs.user.AppUserEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class AppUserResponseDTO {

    private Long id;
    private String name;
    private String userName;
    private AppUserRoleDTO role;
    private AppUserImageDTO image;

    public AppUserResponseDTO(AppUserEntity entity) {
        BeanUtils.copyProperties(entity, this);
        if (entity.getRole() != null)
            this.role = new AppUserRoleDTO(entity.getRole());
        if (entity.getUserImagEntity() != null)
            this.image = new AppUserImageDTO(entity.getUserImagEntity());
    }
}
