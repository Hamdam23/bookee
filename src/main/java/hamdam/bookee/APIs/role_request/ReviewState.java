package hamdam.bookee.APIs.role_request;

import lombok.Getter;
import lombok.Setter;

// TODO: 9/2/22 naming
@Getter
@Setter
public class ReviewState {
    private State state;
    // TODO: 9/2/22 add description, admin can write description when declining role request
}
