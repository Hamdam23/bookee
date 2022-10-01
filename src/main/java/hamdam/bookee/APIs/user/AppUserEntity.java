package hamdam.bookee.APIs.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import hamdam.bookee.APIs.auth.RegistrationRequest;
import hamdam.bookee.APIs.image.ImagEntity;
import hamdam.bookee.APIs.role.AppRoleEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

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
    private String userName;

    @NotBlank(message = "password can not be blank!")
    private String password;

    @ManyToOne
    private AppRoleEntity role;

    // TODO: 9/2/22 name & json
    @OneToOne
    @JsonProperty("user_image")
    private ImagEntity userImage;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime timeStamp;

    public AppUserEntity(RegistrationRequest dto) {
        this.setName(dto.getName());
        this.setUserName(dto.getUsername());
        this.setPassword(dto.getPassword());
    }

    public AppUserEntity(String userName, AppRoleEntity role){
        this.userName = userName;
        this.role = role;
    }
}
