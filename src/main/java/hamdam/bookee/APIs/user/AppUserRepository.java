package hamdam.bookee.APIs.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findAppUserByUserName(String userName);

    Optional<AppUser> findAppUserById(Long id);

    List<AppUser> findAllByOrderByTimeStampDesc();

    boolean existsByUserName(String username);
}
