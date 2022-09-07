package hamdam.bookee.APIs.role;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collections;
import java.util.Set;

// TODO: 9/2/22 naming
@Entity
@Data
@NoArgsConstructor
public class AppRole {
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

    // TODO: 9/2/22 why eager?
    //  I want to make all the permissions displayed when role is called
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(value = EnumType.STRING)
    private Set<Permissions> permissions = Collections.emptySet();

    public AppRole(AppRoleDTO dto) {
        this.roleName = dto.getRoleName();
        this.permissions = dto.getPermissions();
        this.isDefault = dto.isDefault();
    }

}
