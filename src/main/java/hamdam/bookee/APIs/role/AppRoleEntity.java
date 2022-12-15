package hamdam.bookee.APIs.role;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import hamdam.bookee.APIs.role_request.RoleRequestEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

import static hamdam.bookee.tools.constants.Patterns.TIMESTAMP_PATTERN;
import static hamdam.bookee.tools.constants.TableNames.TABLE_NAME_ROLE;
import static hamdam.bookee.tools.constants.TableNames.TABLE_NAME_ROLE_PERMISSIONS;

/**
 * It's a JPA entity class that represents a role in the application
 */
@Entity
@Table(name = TABLE_NAME_ROLE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// It's a JPA annotation that allows us to fetch the permissions of a role in a single query.
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

    @CollectionTable(name = TABLE_NAME_ROLE_PERMISSIONS,
            joinColumns = @JoinColumn(name = "role_id"))
    @ElementCollection
    @Enumerated(value = EnumType.STRING)
    private Set<Permissions> permissions = Collections.emptySet();

    @OneToMany(mappedBy = "requestedRole")
    private List<RoleRequestEntity> roleRequests;

    @JsonFormat(pattern = TIMESTAMP_PATTERN)
    @UpdateTimestamp
    private LocalDateTime timeStamp;

}
