package hamdam.bookee.APIs.roleRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<RequestEntity, Long>, JpaSpecificationExecutor<RequestEntity> {
    List<RequestEntity> findAllByState(State state);
}
