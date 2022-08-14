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
public class TokenResponse {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("access_token_expires_at")
    private Date accessTokenExpiry;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("refresh_token_expires_at")
    private Date refreshTokenExpiry;
    @JsonProperty("role")
    private String role;
    @JsonProperty("permissions")
    Set<Permissions> permissions;
}
