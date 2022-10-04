package hamdam.bookee.APIs.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    // TODO: 9/2/22 in some repositories: findSomethingBySomethingName(), in others: findBySomethingName. write as only one of them
    Optional<ImageEntity> findByImageName(String name);
}
