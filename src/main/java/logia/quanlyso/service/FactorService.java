package logia.quanlyso.service;

import java.util.List;

import logia.quanlyso.service.dto.FactorDTO;

/**
 * Service Interface for managing Factor.
 */
public interface FactorService {

    /**
     * Save a factor.
     *
     * @param factorDTO the entity to save
     * @return the persisted entity
     */
    FactorDTO save(FactorDTO factorDTO);

    /**
     *  Get all the factors.
     *  
     *  @return the list of entities
     */
    List<FactorDTO> findAll();

    /**
     *  Get the "id" factor.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    FactorDTO findOne(Long id);

    /**
     *  Delete the "id" factor.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
