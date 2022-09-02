package hamdam.bookee.APIs.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {
    private String name;
    // TODO: 9/2/22 use snake_case for json properties
    private String userName;
    private String password;
    // TODO: 9/2/22 why I can't add image to my profile when registering
}
