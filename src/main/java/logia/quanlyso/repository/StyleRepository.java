package logia.quanlyso.repository;

import logia.quanlyso.domain.Style;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Style entity.
 *
 * @author Dai Mai
 */
@SuppressWarnings("unused")
public interface StyleRepository extends JpaRepository<Style, Long> {

}
