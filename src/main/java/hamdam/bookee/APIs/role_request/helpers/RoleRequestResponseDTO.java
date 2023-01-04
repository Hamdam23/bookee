package hamdam.bookee.APIs.role_request.helpers;

import hamdam.bookee.APIs.role_request.State;
import hamdam.bookee.APIs.user.helpers.UserResponseDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class RoleRequestResponseDTO {
    private Long id;
    private UserResponseDTO user;
    private String requestedRole;
    private LocalDateTime timeStamp;
    private State state;
    private String description;
}
