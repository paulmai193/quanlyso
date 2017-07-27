package logia.quanlyso.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import logia.quanlyso.service.dto.TransactionDetailsDTO;

/**
 * Service Interface for managing TransactionDetails.
 *
 * @author Dai Mai
 */
public interface TransactionDetailsService {

	/**
	 * Save a transactionDetails.
	 *
	 * @param transactionDetailsDTO the entity to save
	 * @return the persisted entity
	 */
	TransactionDetailsDTO save(TransactionDetailsDTO transactionDetailsDTO);

	/**
	 * Get all the transactionDetails.
	 * 
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	Page<TransactionDetailsDTO> findAll(Pageable pageable);

	/**
	 * Get the "id" transactionDetails.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	TransactionDetailsDTO findOne(Long id);

	/**
	 * Delete the "id" transactionDetails.
	 *
	 * @param id the id of the entity
	 */
	void delete(Long id);
}
