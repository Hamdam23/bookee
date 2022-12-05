package hamdam.bookee.APIs.user.helpers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AppUserResponseDTO {

    private Long id;
    private String name;
    private String username;
    private AppUserRoleDTO role;
    private SetUserImageDTO image;
}
