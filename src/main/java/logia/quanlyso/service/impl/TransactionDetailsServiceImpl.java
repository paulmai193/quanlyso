package logia.quanlyso.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import logia.quanlyso.domain.TransactionDetails;
import logia.quanlyso.repository.TransactionDetailsRepository;
import logia.quanlyso.service.TransactionDetailsService;
import logia.quanlyso.service.dto.TransactionDetailsDTO;
import logia.quanlyso.service.mapper.TransactionDetailsMapper;

/**
 * Service Implementation for managing TransactionDetails.
 */
@Service
@Transactional
public class TransactionDetailsServiceImpl implements TransactionDetailsService{

    private final Logger log = LoggerFactory.getLogger(TransactionDetailsServiceImpl.class);
    
    private final TransactionDetailsRepository transactionDetailsRepository;

    private final TransactionDetailsMapper transactionDetailsMapper;

    public TransactionDetailsServiceImpl(TransactionDetailsRepository transactionDetailsRepository, TransactionDetailsMapper transactionDetailsMapper) {
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
        log.debug("Request to save TransactionDetails : {}", transactionDetailsDTO);
        TransactionDetails transactionDetails = transactionDetailsMapper.toEntity(transactionDetailsDTO);
        transactionDetails = transactionDetailsRepository.save(transactionDetails);
        TransactionDetailsDTO result = transactionDetailsMapper.toDto(transactionDetails);
        return result;
    }

    /**
     *  Get all the transactionDetails.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TransactionDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TransactionDetails");
        Page<TransactionDetails> result = transactionDetailsRepository.findAll(pageable);
        return result.map(transactionDetails -> transactionDetailsMapper.toDto(transactionDetails));
    }

    /**
     *  Get one transactionDetails by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TransactionDetailsDTO findOne(Long id) {
        log.debug("Request to get TransactionDetails : {}", id);
        TransactionDetails transactionDetails = transactionDetailsRepository.findOne(id);
        TransactionDetailsDTO transactionDetailsDTO = transactionDetailsMapper.toDto(transactionDetails);
        return transactionDetailsDTO;
    }

    /**
     *  Delete the  transactionDetails by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TransactionDetails : {}", id);
        transactionDetailsRepository.delete(id);
    }
}
