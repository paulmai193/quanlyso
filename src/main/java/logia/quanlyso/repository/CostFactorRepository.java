package logia.quanlyso.repository;

import logia.quanlyso.domain.CostFactor;
import logia.quanlyso.domain.Style;
import logia.quanlyso.domain.Types;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the CostFactor entity.
 *
 * @author Dai Mai
 */
@SuppressWarnings("unused")
public interface CostFactorRepository extends JpaRepository<CostFactor, Long> {

	/**
	 * Find one by style and types.
	 *
	 * @param style the style
	 * @param types the types
	 * @return the list
	 */
	CostFactor findOneByStylesAndTypes(Style style, Types types);

	/**
	 * Find all by styles and types.
	 *
	 * @param style the style
	 * @param types the types
	 * @return the list
	 */
	List<CostFactor> findAllByStylesAndTypes(Style style, Types types);
}
