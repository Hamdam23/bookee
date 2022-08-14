package hamdam.bookee.APIs.role;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collections;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class AppRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String roleName;

    @JsonProperty("is_default")
    private boolean isDefault = false;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(value = EnumType.STRING)
    private Set<Permissions> permissions = Collections.emptySet();

    public AppRole(AppRoleDTO dto) {
        this.roleName = dto.getRoleName();
        this.permissions = dto.getPermissions();
        this.isDefault = dto.isDefault();
    }

}
