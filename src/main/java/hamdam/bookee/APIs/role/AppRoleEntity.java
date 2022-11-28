package hamdam.bookee.APIs.role;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import hamdam.bookee.APIs.role_request.RoleRequestEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@NamedEntityGraph(name = "with_permissions", attributeNodes = @NamedAttributeNode(value = "permissions"))
public class AppRoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("role_name")
    @Column(unique = true, nullable = false)
    @NotBlank(message = "role_name name can not be blank!")
    private String roleName;

    @JsonProperty("is_default")
    @Column(nullable = false)
    private boolean isDefault = false;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime timeStamp;

    @ElementCollection
    @Enumerated(value = EnumType.STRING)
    private Set<Permissions> permissions = Collections.emptySet();

    @OneToMany(mappedBy = "requestedRole")
    private List<RoleRequestEntity> roleRequests;

}
