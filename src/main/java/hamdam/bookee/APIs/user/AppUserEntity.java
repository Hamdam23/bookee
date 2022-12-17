package hamdam.bookee.APIs.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import hamdam.bookee.APIs.image.ImageEntity;
import hamdam.bookee.APIs.role.AppRoleEntity;
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
import java.util.List;

import static hamdam.bookee.tools.constants.Patterns.TIMESTAMP_PATTERN;
import static hamdam.bookee.tools.constants.TableNames.TABLE_NAME_USER;

/**
 * It's a JPA entity class that represents a user in the database
 */
@Entity
@Table(name = TABLE_NAME_USER)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "name can not be blank!")
    private String name;

    @NotBlank(message = "username can not be blank!")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "password can not be blank!")
    private String password;

    @ManyToOne
    private AppRoleEntity role;

    @OneToOne
    @JsonProperty("user_image")
    private ImageEntity userImage;

    @OneToMany(mappedBy = "user")
    private List<RoleRequestEntity> roleRequests;

    @JsonFormat(pattern = TIMESTAMP_PATTERN)
    @UpdateTimestamp
    private LocalDateTime timeStamp;
}
