package hamdam.bookee.APIs.user.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * It's a DTO class that maps to the app_user_role table
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppUserRoleDTO {

    @JsonProperty("role_id")
    private Long id;
    @JsonProperty("role_name")
    private String roleName;
}
