package hamdam.bookee.APIs.role_request.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * It's a DTO class that contains a roleId field
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleIdRoleRequest {

    @JsonProperty("role_id")
    @NotNull(message = "role_id name can not be null!")
    private Long roleId;
}
