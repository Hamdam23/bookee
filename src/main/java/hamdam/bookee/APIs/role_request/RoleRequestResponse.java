package hamdam.bookee.APIs.role_request;

import hamdam.bookee.APIs.image.ImagEntity;
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
    private ImagEntity userImagEntity;
    private String userRole;
    private String requestedRole;
    private LocalDateTime timeStamp;
    private State state;

    public RoleRequestResponse(RequestEntity entity, String requestedRole){
        this.name = entity.getUser().getName();
        this.userName = entity.getUser().getUserName();
        this.userRole = entity.getUser().getRole().getRoleName();
        this.userImagEntity = entity.getUser().getUserImagEntity();
        this.requestedRole = requestedRole;
        BeanUtils.copyProperties(entity, this);
    }

    public RoleRequestResponse(RequestEntity entity){
        this.name = entity.getUser().getName();
        this.userName = entity.getUser().getUserName();
        this.userRole = entity.getUser().getRole().getRoleName();
        this.userImagEntity = entity.getUser().getUserImagEntity();
        BeanUtils.copyProperties(entity, this);
    }
}
