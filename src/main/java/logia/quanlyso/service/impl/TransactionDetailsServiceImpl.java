package logia.quanlyso.service.impl;

import logia.quanlyso.domain.TransactionDetails;
import logia.quanlyso.repository.TransactionDetailsRepository;
import logia.quanlyso.service.TransactionDetailsService;
import logia.quanlyso.service.dto.TransactionDetailsDTO;
import logia.quanlyso.service.mapper.TransactionDetailsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing TransactionDetails.
 *
 * @author Dai Mai
 */
@Service
@Transactional
public class TransactionDetailsServiceImpl implements TransactionDetailsService {

	/** The log. */
	private final Logger						log	= LoggerFactory
			.getLogger(TransactionDetailsServiceImpl.class);

	/** The transaction details repository. */
	private final TransactionDetailsRepository	transactionDetailsRepository;

	/** The transaction details mapper. */
	private final TransactionDetailsMapper		transactionDetailsMapper;

	/**
	 * Instantiates a new transaction details service impl.
	 *
	 * @param transactionDetailsRepository the transaction details repository
	 * @param transactionDetailsMapper the transaction details mapper
	 */
	public TransactionDetailsServiceImpl(TransactionDetailsRepository transactionDetailsRepository,
			TransactionDetailsMapper transactionDetailsMapper) {
		this.transactionDetailsRepository = transactionDetailsRepository;
		this.transactionDetailsMapper = transactionDetailsMapper;
	}

	/**
	 * Save a transactionDetails.
	 *
	 * @param transactionDetailsDTO the entity to save
	 * @return the persisted entity
	 */
	@Override
	public TransactionDetailsDTO save(TransactionDetailsDTO transactionDetailsDTO) {
		this.log.debug("Request to save TransactionDetails : {}", transactionDetailsDTO);
		TransactionDetails transactionDetails = this.transactionDetailsMapper
				.toEntity(transactionDetailsDTO);
		transactionDetails = this.transactionDetailsRepository.save(transactionDetails);
		TransactionDetailsDTO result = this.transactionDetailsMapper.toDto(transactionDetails);
		return result;
	}

	/**
	 * Get all the transactionDetails.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<TransactionDetailsDTO> findAll(Pageable pageable) {
		this.log.debug("Request to get all TransactionDetails");
		Page<TransactionDetails> result = this.transactionDetailsRepository.findAll(pageable);
		return result.map(transactionDetails -> this.transactionDetailsMapper.toDto(transactionDetails));
	}

	/**
	 * Get one transactionDetails by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Override
	@Transactional(readOnly = true)
	public TransactionDetailsDTO findOne(Long id) {
		this.log.debug("Request to get TransactionDetails : {}", id);
		TransactionDetails transactionDetails = this.transactionDetailsRepository.findOne(id);
		TransactionDetailsDTO transactionDetailsDTO = this.transactionDetailsMapper
				.toDto(transactionDetails);
		return transactionDetailsDTO;
	}

	/**
	 * Delete the transactionDetails by id.
	 *
	 * @param id the id of the entity
	 */
	@Override
	public void delete(Long id) {
		this.log.debug("Request to delete TransactionDetails : {}", id);
		this.transactionDetailsRepository.delete(id);
	}
}
