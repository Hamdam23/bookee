package hamdam.bookee.APIs.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO: 9/2/22 do you really need this class, can't you just use the tokens response directly?
@Data
@NoArgsConstructor
@AllArgsConstructor
// TODO: 9/2/22 naming
public class AccessTokenResponse {
    @JsonProperty("access_token")
    private String accessToken;
    // TODO: 9/2/22 naming & json name
    @JsonProperty("access_token_expires_at")
    private String accessTokenExpiry;
}
