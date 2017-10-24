/*
 * 
 */
package logia.quanlyso.repository;

import logia.quanlyso.domain.CostFactor;
import logia.quanlyso.domain.Style;
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
	 * Find one by style.
	 *
	 * @param style
	 *            the style
	 * @return the list
	 */
	CostFactor findOneByStyles(Style style);

	/**
	 * Find all by styles.
	 *
	 * @param style
	 *            the style
	 * @return the list
	 */
	List<CostFactor> findAllByStyles(Style style);
}
