/*
 * 
 */
package logia.quanlyso.repository;

import logia.quanlyso.domain.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Transactions entity.
 *
 * @author Dai Mai
 */
@SuppressWarnings("unused")
public interface TransactionsRepository extends JpaRepository<Transactions, Long> {

}
