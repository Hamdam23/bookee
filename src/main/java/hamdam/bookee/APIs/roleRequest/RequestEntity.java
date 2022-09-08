package hamdam.bookee.APIs.roleRequest;

import com.fasterxml.jackson.annotation.JsonFormat;
import hamdam.bookee.APIs.role.AppRole;
import hamdam.bookee.APIs.user.AppUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

// TODO: 9/2/22 naming
@Entity
@Getter
@Setter
@NoArgsConstructor
public class RequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: 9/2/22 maybe default value is also user_id (or app_user_id), why do you need @JoinColumn
    @OneToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    // TODO: 9/2/22 AppUser contains AppRole, why you need this property
    // TODO: 9/2/22 if it is for requested role, then rename property or add comment
    @OneToOne
    private AppRole role;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime timeStamp;

    @Enumerated(EnumType.STRING)
    private State state;

    public RequestEntity(AppUser user, AppRole role, State state) {
        this.user = user;
        this.role = role;
        this.state = state;
    }
}
