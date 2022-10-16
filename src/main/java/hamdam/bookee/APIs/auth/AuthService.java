package hamdam.bookee.APIs.auth;

import java.io.IOException;

public interface AuthService {
    TokensResponse registerUser(RegistrationRequest user);
    TokensResponse refreshToken(String header) throws IOException;
}
