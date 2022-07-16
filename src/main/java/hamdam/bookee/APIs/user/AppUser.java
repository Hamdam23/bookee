package hamdam.bookee.APIs.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hamdam.bookee.APIs.image.Image;
import hamdam.bookee.APIs.role.AppRole;
import hamdam.bookee.APIs.role.AppRoleDTO;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String userName;

    @JsonIgnore
    private String password;

    @OneToOne
    private Image userImage;

    @ManyToOne
    private AppRole role;

    public AppUser(AppUserDTO dto){
        this.setName(dto.getName());
        this.setUserName(dto.getUserName());
        this.setPassword(dto.getPassword());
    }
}
