package hamdam.bookee.APIs.role_request.helpers;

import hamdam.bookee.APIs.role_request.State;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * It's a DTO that represents a request to create a review
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDTO {

    @NotNull
    private State state;
    private String description;
}
