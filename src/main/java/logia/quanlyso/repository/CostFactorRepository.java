package logia.quanlyso.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import logia.quanlyso.domain.CostFactor;
import logia.quanlyso.domain.Factor;
import logia.quanlyso.domain.Style;
import logia.quanlyso.domain.Types;

/**
 * Spring Data JPA repository for the CostFactor entity.
 *
 * @author Dai Mai
 */
@SuppressWarnings("unused")
public interface CostFactorRepository extends JpaRepository<CostFactor,Long> {
	
	/**
	 * Find one by factor and style and types.
	 *
	 * @param factor the factor
	 * @param style the style
	 * @param types the types
	 * @return the list
	 */
	CostFactor findOneByFactorsAndStylesAndTypes(Factor factor, Style style, Types types);
	
	/**
	 * Find all by factors and styles and types.
	 *
	 * @param factor the factor
	 * @param style the style
	 * @param types the types
	 * @return the list
	 */
	List<CostFactor> findAllByFactorsAndStylesAndTypes(Factor factor, Style style, Types types);
}
