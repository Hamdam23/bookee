package hamdam.bookee.APIs.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    // TODO: 13/07/22 findImage... vs find...  (1 avvalgi qaytarmimi)
    Optional<Image> findByImageName(String name);
}
