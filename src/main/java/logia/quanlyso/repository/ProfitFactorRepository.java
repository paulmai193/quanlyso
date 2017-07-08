package logia.quanlyso.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import logia.quanlyso.domain.Factor;
import logia.quanlyso.domain.ProfitFactor;
import logia.quanlyso.domain.Style;
import logia.quanlyso.domain.Types;

/**
 * Spring Data JPA repository for the ProfitFactor entity.
 *
 * @author Dai Mai
 */
@SuppressWarnings("unused")
public interface ProfitFactorRepository extends JpaRepository<ProfitFactor, Long> {

	/**
	 * Find one by factor and style and types.
	 *
	 * @param factor the factor
	 * @param style the style
	 * @param types the types
	 * @return the list
	 */
	ProfitFactor findOneByFactorsAndStylesAndTypes(Factor factor, Style style, Types types);
}
