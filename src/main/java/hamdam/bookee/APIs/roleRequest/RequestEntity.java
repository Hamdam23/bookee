package hamdam.bookee.APIs.roleRequest;

import com.fasterxml.jackson.annotation.JsonFormat;
import hamdam.bookee.APIs.role.AppRole;
import hamdam.bookee.APIs.user.AppUser;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class RequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @OneToOne
    private AppRole role;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime timeStamp;

    @Enumerated(EnumType.STRING)
    private State state;
}
