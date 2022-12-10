package hamdam.bookee.APIs.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import hamdam.bookee.APIs.role.Permissions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * It's a POJO that represents the response for tokens endpoint
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokensResponse {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("access_token_expiry")
    private String accessTokenExpiry;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("refresh_token_expiry")
    private String refreshTokenExpiry;
    @JsonProperty("role_name")
    private String roleName;
    @JsonProperty("permissions")
    private Set<Permissions> permissions;

    public TokensResponse(String accessToken, String accessTokenExpiry) {
        this.accessToken = accessToken;
        this.accessTokenExpiry = accessTokenExpiry;
    }
}
