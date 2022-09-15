package hamdam.bookee.APIs.user.helpers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppUserRequestDTO {
    private String name;
    private String userName;
    private Long roleId;
    private Long imageId;
}
