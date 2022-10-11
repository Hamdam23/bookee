package hamdam.bookee.APIs.role_request.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

// TODO: 9/2/22 naming
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleRequestDTO {
    @JsonProperty("role_id")
    @NotNull(message = "role_id name can not be blank!")
    private Long roleId;

    // no idea why I am using description here
//    @Size(max = 200, message = "Description size is too long!")
//    private String description;
}
