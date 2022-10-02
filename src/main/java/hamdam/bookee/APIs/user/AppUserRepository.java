package hamdam.bookee.APIs.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUserEntity, Long> {
    boolean existsByUserName(String username);

    Optional<AppUserEntity> findAppUserByUserName(String userName);

    Page<AppUserEntity> findAllByOrderByTimeStampDesc(Pageable pageable);
}
