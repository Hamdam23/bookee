package hamdam.bookee.APIs.user.helpers;

import hamdam.bookee.APIs.image.helpers.ImageResponseDTO;
import hamdam.bookee.APIs.role.helpers.AppRoleResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * It's a DTO that contains the id, name, username, role, and image of an AppUser
 */
@Getter
@Setter
@NoArgsConstructor
public class AppUserResponseDTO {

    private Long id;
    private String name;
    private String username;
    private AppRoleResponseDTO role;
    private ImageResponseDTO image;
}
