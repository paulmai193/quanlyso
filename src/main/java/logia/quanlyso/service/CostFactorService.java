package logia.quanlyso.service;

import logia.quanlyso.service.dto.CostFactorDTO;

import java.util.List;

/**
 * Service Interface for managing CostFactor.
 *
 * @author Dai Mai
 */
public interface CostFactorService {

	/**
	 * Save a costFactor.
	 *
	 * @param costFactorDTO the entity to save
	 * @return the persisted entity
	 */
	CostFactorDTO save(CostFactorDTO costFactorDTO);

	/**
	 * Get all the costFactors.
	 *
	 * @return the list of entities
	 */
	List<CostFactorDTO> findAll();

	/**
	 * Get the "id" costFactor.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	CostFactorDTO findOne(Long id);

    /**
     * Get the costFactor by style ID.
     *
     * @param id the id of the style entity
     * @return the entity
     */
    CostFactorDTO findOneByStyleId(Long id);

	/**
	 * Delete the "id" costFactor.
	 *
	 * @param id the id of the entity
	 */
	void delete(Long id);
}
