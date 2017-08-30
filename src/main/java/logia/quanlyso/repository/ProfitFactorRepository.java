package logia.quanlyso.repository;

import logia.quanlyso.domain.ProfitFactor;
import logia.quanlyso.domain.Style;
import logia.quanlyso.domain.Types;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the ProfitFactor entity.
 *
 * @author Dai Mai
 */
@SuppressWarnings("unused")
public interface ProfitFactorRepository extends JpaRepository<ProfitFactor, Long> {

	/**
	 * Find one by style and types.
	 *
	 * @param style the style
	 * @param types the types
	 * @return the list
	 */
	ProfitFactor findOneByStylesAndTypes(Style style, Types types);
}
