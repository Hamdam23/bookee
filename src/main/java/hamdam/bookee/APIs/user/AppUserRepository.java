package hamdam.bookee.APIs.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUserEntity, Long> {
    Optional<AppUserEntity> findAppUserByUserName(String userName);

    // TODO: 9/2/22 there is default findById for this logic

    Page<AppUserEntity> findAllByOrderByTimeStampDesc(Pageable pageable);

    boolean existsByUserName(String username);
}
