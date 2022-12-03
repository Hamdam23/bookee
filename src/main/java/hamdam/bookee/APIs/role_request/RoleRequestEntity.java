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

import static hamdam.bookee.tools.constants.Patterns.TIMESTAMP_PATTERN;
import static hamdam.bookee.tools.constants.TableNames.TABLE_NAME_ROLE_REQUEST;

@Entity
@Table(name = TABLE_NAME_ROLE_REQUEST)
@Getter
@Setter
@NoArgsConstructor
public class RoleRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private AppUserEntity user;

    @ManyToOne(optional = false)
    private AppRoleEntity requestedRole;

    @Enumerated(EnumType.STRING)
    private State state;

    @Size(max = 200, message = "Description size is too long!")
    private String description;

    @JsonFormat(pattern = TIMESTAMP_PATTERN)
    @UpdateTimestamp
    private LocalDateTime timeStamp;
}
