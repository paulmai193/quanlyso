package logia.quanlyso.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import logia.quanlyso.domain.TransactionDetails;

/**
 * Spring Data JPA repository for the TransactionDetails entity.
 *
 * @author Dai Mai
 */
@SuppressWarnings("unused")
public interface TransactionDetailsRepository extends JpaRepository<TransactionDetails,Long> {

}
