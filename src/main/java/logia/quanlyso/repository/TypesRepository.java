package logia.quanlyso.repository;

import logia.quanlyso.domain.Types;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Types entity.
 *
 * @author Dai Mai
 */
@SuppressWarnings("unused")
public interface TypesRepository extends JpaRepository<Types, Long> {

}
