package hamdam.bookee.APIs.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findAppUserByUserName(String userName);

    // TODO: 9/2/22 there is default findById for this logic

    List<AppUser> findAllByOrderByTimeStampDesc();

    boolean existsByUserName(String username);
}
