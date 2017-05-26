package logia.quanlyso.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import logia.quanlyso.domain.Types;

/**
 * Spring Data JPA repository for the Types entity.
 *
 * @author Dai Mai
 */
@SuppressWarnings("unused")
public interface TypesRepository extends JpaRepository<Types,Long> {

}
