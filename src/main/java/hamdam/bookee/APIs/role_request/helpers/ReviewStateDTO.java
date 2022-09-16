package hamdam.bookee.APIs.role_request.helpers;

import hamdam.bookee.APIs.role_request.State;
import lombok.Getter;
import lombok.Setter;

// TODO: 9/2/22 naming
@Getter
@Setter
public class ReviewStateDTO {
    private State state;
    // TODO: 9/2/22 add description, admin can write description when declining role request
    private String description;
}
