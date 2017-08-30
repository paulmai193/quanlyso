package logia.quanlyso.repository;

import logia.quanlyso.domain.TransactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the TransactionDetails entity.
 *
 * @author Dai Mai
 */
@SuppressWarnings("unused")
public interface TransactionDetailsRepository extends JpaRepository<TransactionDetails, Long> {

}
