package logia.quanlyso.repository;

import logia.quanlyso.domain.Transactions;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Transactions entity.
 */
@SuppressWarnings("unused")
public interface TransactionsRepository extends JpaRepository<Transactions,Long> {

}
