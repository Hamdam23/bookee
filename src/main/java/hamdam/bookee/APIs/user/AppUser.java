package hamdam.bookee.APIs.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import hamdam.bookee.APIs.auth.RegistrationRequest;
import hamdam.bookee.APIs.image.Image;
import hamdam.bookee.APIs.role.AppRole;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String userName;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime timeStamp;

    @JsonIgnore
    private String password;

    @ManyToOne
    private AppRole role;

    // TODO: 9/2/22 name & json
    @OneToOne
    @JsonProperty("user_image")
    private Image userImage;

    public AppUser(RegistrationRequest dto){
        this.setName(dto.getName());
        this.setUserName(dto.getUsername());
        this.setPassword(dto.getPassword());
    }
}
