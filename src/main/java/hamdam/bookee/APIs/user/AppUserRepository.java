package hamdam.bookee.APIs.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUserEntity, Long> {
    boolean existsByUsername(String username);

    Optional<AppUserEntity> findAppUserByUsername(String username);

    @EntityGraph(attributePaths = "role.permissions", type = EntityGraph.EntityGraphType.LOAD)
    @Query("select app_user from AppUserEntity app_user where app_user.username = :username")
    Optional<AppUserEntity> findAppUserByUsernameWithPermission(String username);

    Page<AppUserEntity> findAllByOrderByTimeStampDesc(Pageable pageable);
}
