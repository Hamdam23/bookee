package hamdam.bookee.APIs.auth;

public interface AuthService {
    TokensResponse registerUser(RegistrationRequest user);

    TokensResponse refreshToken(String header);
}
