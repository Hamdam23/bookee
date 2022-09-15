package hamdam.bookee.APIs.role;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import hamdam.bookee.APIs.role.helpers.AppRoleRequestDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

// TODO: 9/2/22 naming
@Entity
@Table(name = "app_roles")
@Getter
@Setter
@NoArgsConstructor
public class AppRoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // TODO: 9/2/22 naming & json property
    @Column(unique = true, nullable = false)
    @JsonProperty("role_name")
    private String roleName;

    // TODO: 9/2/22 set column to nullable false
    @Column(nullable = false)
    @JsonProperty("is_default")
    private boolean isDefault = false;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime timeStamp;

    // TODO: 9/2/22 why eager?
    //  I want to make all the permissions displayed when role is called
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(value = EnumType.STRING)
    private Set<Permissions> permissions = Collections.emptySet();

    public AppRoleEntity(AppRoleRequestDTO dto) {
        this.roleName = dto.getRoleName();
        this.permissions = dto.getPermissions();
        this.isDefault = dto.isDefault();
    }

}
