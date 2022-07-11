package hamdam.bookee.APIs.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hamdam.bookee.APIs.image.Image;
import hamdam.bookee.APIs.role.AppRole;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String name;

    @JsonIgnore
    private String password;

    @OneToOne
    private Image userImage;

    @ManyToOne
    private AppRole roles;

    public AppUser(AppUserDTO user) {
        this.setName(user.getName());
        this.setPassword(user.getPassword());
    }

    public AppUser() {

    }
}
