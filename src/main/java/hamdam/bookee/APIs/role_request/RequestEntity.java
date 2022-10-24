package hamdam.bookee.APIs.role_request;

import com.fasterxml.jackson.annotation.JsonFormat;
import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.user.AppUserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "role_requests")
@Getter
@Setter
@NoArgsConstructor
public class RequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private AppUserEntity user;

    @ManyToOne(optional = false)
    private AppRoleEntity requestedRole;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime timeStamp;

    @Enumerated(EnumType.STRING)
    private State state;

    @Size(max = 200, message = "Description size is too long!")
    private String description;

    public RequestEntity(AppUserEntity user, AppRoleEntity requestedRole, State state) {
        this.user = user;
        this.requestedRole = requestedRole;
        this.state = state;
    }

    public RequestEntity(AppUserEntity user, AppRoleEntity requestedRole) {
        this.user = user;
        this.requestedRole = requestedRole;
    }
}
