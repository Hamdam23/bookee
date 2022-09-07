package hamdam.bookee.APIs.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import hamdam.bookee.APIs.role.Permissions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokensResponse {
    @JsonProperty("access_token")
    private String accessToken;
    // TODO: 9/2/22 naming & json name
    @JsonProperty("access_token_expiry")
    private String accessTokenExpiry;
    @JsonProperty("refresh_token")
    private String refreshToken;
    // TODO: 9/2/22 naming & json name
    @JsonProperty("refresh_token_expiry")
    private String refreshTokenExpiry;
    @JsonProperty("role")
    private String role;
    @JsonProperty("permissions")
    Set<Permissions> permissions;

    public TokensResponse(String accessToken, String accessTokenExpiry) {
        this.accessToken = accessToken;
        this.accessTokenExpiry = accessTokenExpiry;
    }
}
