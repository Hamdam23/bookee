package hamdam.bookee.APIs.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {
    private String name;
    private String username;
    private String password;
    private Long imageId;

    public RegistrationRequest(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }
}
