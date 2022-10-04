package hamdam.bookee.APIs.role_request;

import hamdam.bookee.APIs.image.ImageEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class RoleRequestResponse {
    // TODO: 9/2/22 why using separate properties, Response (DTO) can be like RequestEntity
    private Long id;
    private String name;
    private String userName;
    private ImageEntity userImageEntity;
    private String userRole;
    private String requestedRole;
    private LocalDateTime timeStamp;
    private State state;
    private String description;

    public RoleRequestResponse(RequestEntity entity, String requestedRole){
        this.name = entity.getUser().getName();
        this.userName = entity.getUser().getUserName();
        this.userRole = entity.getUser().getRole().getRoleName();
        this.userImageEntity = entity.getUser().getUserImage();
        this.requestedRole = requestedRole;
        BeanUtils.copyProperties(entity, this);
    }
}
