package logia.quanlyso.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import logia.quanlyso.domain.TransactionDetails;
import logia.quanlyso.domain.Transactions;
import logia.quanlyso.repository.TransactionDetailsRepository;
import logia.quanlyso.repository.TransactionsRepository;
import logia.quanlyso.repository.UserRepository;
import logia.quanlyso.service.CodeService;
import logia.quanlyso.service.TransactionsService;
import logia.quanlyso.service.dto.TransactionsDTO;
import logia.quanlyso.service.mapper.TransactionsMapper;

/**
 * Service Implementation for managing Transactions.
 *
 * @author Dai Mai
 */
@Service
@Transactional
public class TransactionsServiceImpl implements TransactionsService {

	/** The log. */
	private final Logger						log	= LoggerFactory
			.getLogger(TransactionsServiceImpl.class);

	/** The transactions repository. */
	private final TransactionsRepository		transactionsRepository;

	/** The transactions mapper. */
	private final TransactionsMapper			transactionsMapper;

	/** The user repository. */
	private final UserRepository				userRepository;

	/** The transaction details repository. */
	private final TransactionDetailsRepository	transactionDetailsRepository;

	/** The code service. */
	private final CodeService					codeService;

	/**
	 * Instantiates a new transactions service impl.
	 *
	 * @param transactionsRepository the transactions repository
	 * @param transactionsMapper the transactions mapper
	 * @param userRepository the user repository
	 * @param transactionDetailsRepository the transaction details repository
	 * @param codeService the code service
	 */
	public TransactionsServiceImpl(TransactionsRepository transactionsRepository,
			TransactionsMapper transactionsMapper, UserRepository userRepository,
			TransactionDetailsRepository transactionDetailsRepository, CodeService codeService) {
		this.transactionsRepository = transactionsRepository;
		this.transactionsMapper = transactionsMapper;
		this.userRepository = userRepository;
		this.transactionDetailsRepository = transactionDetailsRepository;
		this.codeService = codeService;
	}

	/**
	 * Save a transactions.
	 *
	 * @param transactionsDTO the entity to save
	 * @return the persisted entity
	 */
	@Override
	public TransactionsDTO save(TransactionsDTO transactionsDTO) {
		this.log.debug("Request to save Transactions : {}", transactionsDTO);

		Transactions transactions = this.transactionsMapper.toEntity(transactionsDTO);

		// Get & set user entity to this transaction
		transactions.setUsers(this.userRepository.getOne(transactionsDTO.getClientsId()));

		// Save transaction
		transactions = this.transactionsRepository.save(transactions);

		// Save & set the detail (if have) to this transaction
		for (TransactionDetails details : transactions.getTransactionDetails()) {
			details.setTransactions(transactions);
			this.transactionDetailsRepository.save(details);
		}

		TransactionsDTO result = this.transactionsMapper.toDto(transactions);
		return result;
	}

	/**
	 * Get all the transactions.
	 * 
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<TransactionsDTO> findAll(Pageable pageable) {
		this.log.debug("Request to get all Transactions");
		Page<Transactions> result = this.transactionsRepository.findAll(pageable);
		return result.map(transactions -> this.transactionsMapper.toDto(transactions));
	}

	/**
	 * Get one transactions by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Override
	@Transactional(readOnly = true)
	public TransactionsDTO findOne(Long id) {
		this.log.debug("Request to get Transactions : {}", id);
		Transactions transactions = this.transactionsRepository.findOne(id);
		TransactionsDTO transactionsDTO = this.transactionsMapper.toDto(transactions);
		return transactionsDTO;
	}

	/**
	 * Delete the transactions by id.
	 *
	 * @param id the id of the entity
	 */
	@Override
	public void delete(Long id) {
		this.log.debug("Request to delete Transactions : {}", id);
		Transactions transactions = this.transactionsRepository.getOne(id);

		// Delete all detail of this transaction
		for (TransactionDetails transactionDetails : transactions.getTransactionDetails()) {
			this.transactionDetailsRepository.delete(transactionDetails);
		}

		// Remove this transaction out of user collection
		// User user = transactions.getUsers().removeTransactionss(transactions);
		// this.userRepository.save(user);

		// Delete this transaction
		this.transactionsRepository.delete(transactions);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see logia.quanlyso.service.TransactionsService#calculate(logia.quanlyso.service.dto.
	 * TransactionsDTO)
	 */
	@Override
	public TransactionsDTO calculate(TransactionsDTO transactionsDTO) {
		this.log.debug("Request to calculate value of Transactions : {}", transactionsDTO);

		Transactions transactions = this.transactionsMapper.toEntity(transactionsDTO);

		// Get & set user entity to this transaction
		transactions = this.codeService.calculate(transactions);

		TransactionsDTO result = this.transactionsMapper.toDto(transactions);
		return result;
	}
}
