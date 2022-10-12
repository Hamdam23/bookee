package hamdam.bookee.APIs.role_request.helpers;

import hamdam.bookee.APIs.role_request.State;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

// TODO: 9/2/22 naming
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDTO {
    @NotNull
    private State state;
    // TODO: 9/2/22 add description, admin can write description when declining role request
    private String description;

    public ReviewRequestDTO(State state) {
        this.state = state;
    }
}
