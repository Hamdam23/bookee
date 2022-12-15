package hamdam.bookee.APIs.role;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppRoleRepository extends JpaRepository<AppRoleEntity, Long> {

    Optional<AppRoleEntity> findFirstByIsDefaultIsTrue();

    Page<AppRoleEntity> findAllByOrderByTimeStampDesc(Pageable pageable);

    boolean existsByRoleName(String roleName);

    /**
     * Find all AppRoleEntities, order them by timeStamp DESC,
     * and load the permissions for each AppRoleEntity.
     *
     * @param pageable This is the Pageable object that contains
     * the page number, page size, and sort order.
     * @return A Page of AppRoleEntity objects.
     */
    @EntityGraph(value = "with_permissions", type = EntityGraph.EntityGraphType.LOAD)
    @Query("select a from AppRoleEntity a order by a.timeStamp DESC")
    Page<AppRoleEntity> findAllByOrderByTimeStampDescWithPermissions(Pageable pageable);

    /**
     * Find all AppRoleEntity objects,
     * and use the with_permissions entity graph to load the permissions.
     *
     * @param pageable This is the pageable object that contains
     * the page number, page size, and sort order.
     * @return A Page of AppRoleEntity objects.
     */
    @EntityGraph(value = "with_permissions")
    Page<AppRoleEntity> findAll(Pageable pageable);
}
