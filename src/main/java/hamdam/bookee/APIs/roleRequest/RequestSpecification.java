package hamdam.bookee.APIs.roleRequest;

import hamdam.bookee.APIs.user.AppUser;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;

public final class RequestSpecification {
    public static Specification<RequestEntity> filter(Long userId, State state) {
        Specification<RequestEntity> specification = Specification
                .where(userId == null ? null : getRequestsByUserId(userId)
                        .and(state == null ? null : getRequestsByState(state)));

        return specification;
    }

    public static Specification<RequestEntity> getRequestsByUserId(Long userId) {
        return ((root, query, criteriaBuilder) -> {
            Join<RequestEntity, AppUser> join = root.join("user");
            return join.get("id").in(userId);
        });
    }

    public static Specification<RequestEntity> getRequestsByState(State state){
        return ((root, query, criteriaBuilder) -> {
            return root.get("state").equals(state.name());
        });
    }
}
