package hamdam.bookee.APIs.role;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppRoleRepository extends JpaRepository<AppRoleEntity, Long> {

    Optional<AppRoleEntity> findFirstByIsDefaultIsTrue();

    Page<AppRoleEntity> findAllByOrderByTimeStampDesc(Pageable pageable);

    boolean existsByRoleName(String roleName);
}
