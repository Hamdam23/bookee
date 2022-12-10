package hamdam.bookee.APIs.role_request;

import hamdam.bookee.APIs.role_request.helpers.UserOnRoleRequestDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class RoleRequestResponse {
    private Long id;
    private UserOnRoleRequestDTO user;
    private String requestedRole;
    private LocalDateTime timeStamp;
    private State state;
    private String description;
}
