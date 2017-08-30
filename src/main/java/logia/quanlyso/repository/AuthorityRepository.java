package logia.quanlyso.repository;

import logia.quanlyso.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Authority entity.
 *
 * @author Dai Mai
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {

	/**
	 * Gets the one by name.
	 *
	 * @param name the name
	 * @return the authority by name
	 */
	Authority getOneByName(String name);
}
