package hamdam.bookee.APIs.roleRequest;

import lombok.Data;

// TODO: 9/2/22 naming
@Data
public class RequestRole {
    // TODO: 9/2/22 why requesting some role works by role name, there is id in AppRole
    private String roleName;
}
