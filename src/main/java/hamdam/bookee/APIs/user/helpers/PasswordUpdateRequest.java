package hamdam.bookee.APIs.user.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * It's a POJO that contains the old password, the new password,
 * and the confirmation of the new password
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordUpdateRequest {

    @JsonProperty("old_password")
    private String oldPassword;

    @JsonProperty("new_password")
    private String newPassword;

    @JsonProperty("confirm_new_password")
    private String confirmNewPassword;
}
