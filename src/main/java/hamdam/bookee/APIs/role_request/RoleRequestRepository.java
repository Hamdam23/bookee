package hamdam.bookee.APIs.role_request;

import hamdam.bookee.APIs.user.AppUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRequestRepository extends JpaRepository<RoleRequestEntity, Long>, JpaSpecificationExecutor<RoleRequestEntity> {
    boolean existsByUserAndState(AppUserEntity user, State state);

    List<RoleRequestEntity> findAllByState(State state);

    List<RoleRequestEntity> findAllByUser(AppUserEntity user);
}
