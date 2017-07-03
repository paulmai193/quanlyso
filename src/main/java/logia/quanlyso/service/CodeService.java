package logia.quanlyso.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import logia.quanlyso.domain.Transactions;
import logia.quanlyso.service.dto.CodeDTO;

/**
 * Service Interface for managing Code.
 */
public interface CodeService {

    /**
     * Save a code.
     *
     * @param codeDTO the entity to save
     * @return the persisted entity
     */
    CodeDTO save(CodeDTO codeDTO);

    /**
     *  Get all the codes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CodeDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" code.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CodeDTO findOne(Long id);

    /**
     *  Delete the "id" code.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
    
    /**
     * Calculate the value of transaction.
     *
     * @param transactions the transactions
     * @return the transactions
     */
    Transactions calculate(Transactions transactions);
    
}
