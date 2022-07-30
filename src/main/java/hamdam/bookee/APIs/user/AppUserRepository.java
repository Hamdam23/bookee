package hamdam.bookee.APIs.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findAppUserByUserName(String userName);

    List<AppUser> findAllByOrderByTimeStampDesc();

    boolean existsByUserName(String username);
}
