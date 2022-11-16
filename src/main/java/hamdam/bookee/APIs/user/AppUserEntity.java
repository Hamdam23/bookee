package hamdam.bookee.APIs.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import hamdam.bookee.APIs.auth.RegistrationRequest;
import hamdam.bookee.APIs.image.ImageEntity;
import hamdam.bookee.APIs.role.AppRoleEntity;
import hamdam.bookee.APIs.role_request.RequestEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
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
    private List<RequestEntity> roleRequests;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime timeStamp;

    public AppUserEntity(RegistrationRequest dto) {
        this.setName(dto.getName());
        this.setUsername(dto.getUsername());
    }

    public AppUserEntity(String username, AppRoleEntity role) {
        this.username = username;
        this.role = role;
    }

    public AppUserEntity(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public AppUserEntity(String name, String username, String password, LocalDateTime timeStamp) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.timeStamp = timeStamp;
    }

    public AppUserEntity(String name, String username, AppRoleEntity role) {
        this.name = name;
        this.username = username;
        this.role = role;
    }

    public AppUserEntity(String name, String username, String password, AppRoleEntity role) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
