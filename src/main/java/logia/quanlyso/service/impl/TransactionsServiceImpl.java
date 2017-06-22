package logia.quanlyso.service.impl;

import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import logia.quanlyso.domain.TransactionDetails;
import logia.quanlyso.domain.Transactions;
import logia.quanlyso.domain.User;
import logia.quanlyso.repository.TransactionDetailsRepository;
import logia.quanlyso.repository.TransactionsRepository;
import logia.quanlyso.repository.UserRepository;
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
public class TransactionsServiceImpl implements TransactionsService{

    /** The log. */
    private final Logger log = LoggerFactory.getLogger(TransactionsServiceImpl.class);
    
    /** The transactions repository. */
    private final TransactionsRepository transactionsRepository;

    /** The transactions mapper. */
    private final TransactionsMapper transactionsMapper;
    
    /** The user repository. */
    private final UserRepository userRepository;
    
    /** The transaction details repository. */
    private final TransactionDetailsRepository transactionDetailsRepository;

    /**
     * Instantiates a new transactions service impl.
     *
     * @param transactionsRepository the transactions repository
     * @param transactionsMapper the transactions mapper
     * @param userRepository the user repository
     * @param transactionDetailsRepository the transaction details repository
     */
    public TransactionsServiceImpl(TransactionsRepository transactionsRepository, TransactionsMapper transactionsMapper, UserRepository userRepository, TransactionDetailsRepository transactionDetailsRepository) {
        this.transactionsRepository = transactionsRepository;
        this.transactionsMapper = transactionsMapper;
        this.userRepository = userRepository;
        this.transactionDetailsRepository = transactionDetailsRepository;
    }

    /**
     * Save a transactions.
     *
     * @param transactionsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TransactionsDTO save(TransactionsDTO transactionsDTO) {
        log.debug("Request to save Transactions : {}", transactionsDTO);
        
        Transactions transactions = transactionsMapper.toEntity(transactionsDTO);
        
        // Get & set user entity to this transaction
        transactions.setUsers(userRepository.getOne(transactionsDTO.getClientsId()));
        
        // Save & set the detail (if have) to this transaction
        List<TransactionDetails> saveDetails = transactionDetailsRepository.save(transactions.getTransactionDetails());
        transactions.setTransactionDetails(new HashSet<>());
        for (TransactionDetails saveDetail : saveDetails) {
			transactions.addTransactionDetails(saveDetail);
		}
        
        // Save transaction
        transactions = transactionsRepository.save(transactions);
        TransactionsDTO result = transactionsMapper.toDto(transactions);
        return result;
    }

    /**
     *  Get all the transactions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TransactionsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Transactions");
        Page<Transactions> result = transactionsRepository.findAll(pageable);
        return result.map(transactions -> transactionsMapper.toDto(transactions));
    }

    /**
     *  Get one transactions by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TransactionsDTO findOne(Long id) {
        log.debug("Request to get Transactions : {}", id);
        Transactions transactions = transactionsRepository.findOne(id);
        TransactionsDTO transactionsDTO = transactionsMapper.toDto(transactions);
        return transactionsDTO;
    }

    /**
     *  Delete the  transactions by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Transactions : {}", id);
        Transactions transactions = transactionsRepository.getOne(id);
        
        // Delete all detail of this transaction
        for (TransactionDetails transactionDetails: transactions.getTransactionDetails()) {
			transactionDetailsRepository.delete(transactionDetails);
		}
        
        // Remove this transaction out of user collection
        User user = transactions.getUsers().removeTransactionss(transactions);
        this.userRepository.save(user);
        
        // Delete this transaction
        transactionsRepository.delete(transactions);
    }
}
