package logia.quanlyso.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import logia.quanlyso.domain.Factor;

/**
 * Spring Data JPA repository for the Factor entity.
 *
 * @author Dai Mai
 */
@SuppressWarnings("unused")
public interface FactorRepository extends JpaRepository<Factor, Long> {

}
