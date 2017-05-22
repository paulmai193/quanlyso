package logia.quanlyso.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import logia.quanlyso.domain.Transactions;
import logia.quanlyso.repository.TransactionsRepository;
import logia.quanlyso.service.TransactionsService;
import logia.quanlyso.service.dto.TransactionsDTO;
import logia.quanlyso.service.mapper.TransactionsMapper;

/**
 * Service Implementation for managing Transactions.
 */
@Service
@Transactional
public class TransactionsServiceImpl implements TransactionsService{

    private final Logger log = LoggerFactory.getLogger(TransactionsServiceImpl.class);
    
    private final TransactionsRepository transactionsRepository;

    private final TransactionsMapper transactionsMapper;

    public TransactionsServiceImpl(TransactionsRepository transactionsRepository, TransactionsMapper transactionsMapper) {
        this.transactionsRepository = transactionsRepository;
        this.transactionsMapper = transactionsMapper;
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
        transactionsRepository.delete(id);
    }
}
