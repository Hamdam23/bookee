package hamdam.bookee.APIs.user;

import hamdam.bookee.APIs.image.Image;
import hamdam.bookee.APIs.role.AppRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppUserDTO {
    private Long id;
    private String name;
    private String userName;
    private LocalDateTime timeStamp;
    private AppRole role;
    private Image userImage;
}
