package hamdam.bookee.APIs.user.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SetRoleUserRequest {
    @JsonProperty("role_id")
    private Long roleId;
}
