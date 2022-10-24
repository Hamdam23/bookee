package hamdam.bookee.APIs.user.helpers;

import hamdam.bookee.APIs.user.AppUserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@NoArgsConstructor
public class AppUserResponseDTO {

    private Long id;
    private String name;
    private String userName;
    private AppUserRoleDTO role;
    private SetUserImageDTO image;

    public AppUserResponseDTO(AppUserEntity entity) {
        BeanUtils.copyProperties(entity, this);
        if (entity.getRole() != null)
            this.role = new AppUserRoleDTO(entity.getRole());
        if (entity.getUserImage() != null)
            this.image = new SetUserImageDTO(entity.getUserImage());
    }
}
