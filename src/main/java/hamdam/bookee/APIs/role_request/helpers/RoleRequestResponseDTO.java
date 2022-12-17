package hamdam.bookee.APIs.role_request.helpers;

import hamdam.bookee.APIs.role_request.State;
import hamdam.bookee.APIs.user.helpers.AppUserResponseDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class RoleRequestResponseDTO {
    private Long id;
    private AppUserResponseDTO user;
    private String requestedRole;
    private LocalDateTime timeStamp;
    private State state;
    private String description;
}
