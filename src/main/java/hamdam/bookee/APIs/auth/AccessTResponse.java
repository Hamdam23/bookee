package hamdam.bookee.APIs.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessTResponse {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("access_token_expires_at")
    private String accessTokenExpiry;
}
