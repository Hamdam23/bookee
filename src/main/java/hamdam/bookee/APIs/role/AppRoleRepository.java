package hamdam.bookee.APIs.role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// TODO: 9/2/22 @Repository
public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
    Optional<AppRole> findFirstByIsDefault(boolean isDefault);
    Optional<AppRole> findByRoleName(String roleName);
}
